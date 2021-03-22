package com.daimler.vehicle.agent.publishingagent.security;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

@Component
public class SecurityFilter implements Filter {

    private final Logger logger = LoggerFactory.getLogger(SecurityFilter.class);

    @Value("${vehicleId}")
    private String vehicleId;

    private int MAX_REQUESTS_PER_SECOND = 3;

    private LoadingCache<String, Integer> requestCountsPerIpAddress;

    public SecurityFilter(){
        super();
        requestCountsPerIpAddress = CacheBuilder.newBuilder().
                expireAfterWrite(1, TimeUnit.SECONDS).build(new CacheLoader<String, Integer>() {
            public Integer load(String key) {
                return 0;
            }
        });
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        String clientIpAddress = getClientIP((HttpServletRequest) servletRequest);
        logger.info("CLIENT IP = "+clientIpAddress);
        String token = ((HttpServletRequest) servletRequest).getHeader("Authorization");

        logger.info("Authorization token val = "+token);

        if(token == null || token.length()<1 || !authenticate(token)){
            httpServletResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
//            httpServletResponse.getWriter().write("Too many requests");
            return;
        }


        if(isMaximumRequestsPerSecondExceeded(clientIpAddress)){
            httpServletResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            httpServletResponse.getWriter().write("Too many requests");
            return;
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    private boolean isMaximumRequestsPerSecondExceeded(String clientIpAddress){
        int requests = 0;
        try {
            requests = requestCountsPerIpAddress.get(clientIpAddress);
            if(requests > MAX_REQUESTS_PER_SECOND){
                requestCountsPerIpAddress.put(clientIpAddress, requests);
                return true;
            }
        } catch (ExecutionException e) {
            requests = 0;
        }
        requests++;
        requestCountsPerIpAddress.put(clientIpAddress, requests);
        logger.info("NUMBER OF REQUESTS = "+requests);
        return false;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null){
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0]; // voor als ie achter een proxy zit
    }

    private  String decrypt(String token){
        StringBuilder stringBuilder = new StringBuilder();
        final int OFFSET = 13;
        for (int i = 0; i < token.length(); i++) {
            stringBuilder.append((char)(token.charAt(i) - OFFSET));
        }
        String reversedString = new StringBuffer(stringBuilder.toString()).reverse().toString();
        return new String(Base64.getDecoder().decode(reversedString));
    }

    private  boolean authenticate (String token){
        return decrypt(token).equals(vehicleId+"_Vehicle-Agent");
    }

    /**
     * ---------------   Modified Caesar Cipher   ---------------
     * Encryption logic
     * @param message
     * @return
     */
    /*private static String encrypt(String message){
        String encodedMessage = Base64.getEncoder().encodeToString(message.getBytes());

        //Reversing the String
        String reverseString = new StringBuffer(encodedMessage).reverse().toString();

        StringBuilder stringBuilder = new StringBuilder();
        final int OFFSET = 13;

        for (int i = 0; i < reverseString.length(); i++) {
            stringBuilder.append((char)(reverseString.charAt(i)+OFFSET));
        }
        return stringBuilder.toString();
    }*/

}
