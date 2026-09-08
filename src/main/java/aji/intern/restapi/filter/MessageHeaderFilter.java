package aji.intern.restapi.filter;

import aji.intern.restapi.utils.DateTimeUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class MessageHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        request.setAttribute(MessageHeaderVal.MSG_ID.toString(), UUID.randomUUID().toString());

        DateTimeUtil date = new DateTimeUtil();
        request.setAttribute(MessageHeaderVal.TDATE.toString(), date.getTransactionDate());
        request.setAttribute(MessageHeaderVal.TTIME.toString(), date.getTransactionTime());

        filterChain.doFilter(request, response);
    }
}
