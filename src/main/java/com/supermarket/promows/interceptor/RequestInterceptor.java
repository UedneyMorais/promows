package com.supermarket.promows.interceptor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermarket.promows.exception.ExpiredLicenseException;
import com.supermarket.promows.model.dto.LicenseInfoDTO;
import com.supermarket.promows.service.LicenseService;
import com.supermarket.utils.ValidateLicense;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component  
public class RequestInterceptor implements HandlerInterceptor {

    private final LicenseService licenseService;
    public RequestInterceptor(LicenseService licenseService) {
        this.licenseService = licenseService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
       // Verifica se já está na página de licença expirada
        if (request.getRequestURI().equals("/licenses/expired-license.html")) {
            return true; // Permite o acesso à página de licença expirada
        }

        LicenseInfoDTO licenseInfo = licenseService.getLicenseEndDate();

        if (ValidateLicense.isLicenseExpired(licenseInfo.getEndDate())) {
            // Verifica se o cabeçalho de redirecionamento já foi enviado
            if (!response.isCommitted()) {
                response.sendRedirect("/licenses/expired-license.html");
            }
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @org.springframework.lang.Nullable ModelAndView modelAndView) throws Exception {
        // Implement your logic here after the controller method is executed
    }

    // @Override
    // public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    //         throws Exception {
    //     // Implement your logic here after the complete request has been processed
    // }
}
