package com.supermarket.promows.interceptor;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.supermarket.promows.model.Parameter;
import com.supermarket.promows.service.LicenseService;
import com.supermarket.promows.service.ParameterService;
import com.supermarket.utils.ValidateLicense;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component  
public class RequestInterceptor implements HandlerInterceptor {

    private final LicenseService licenseService;
    private final ParameterService parameterService;

    public RequestInterceptor(LicenseService licenseService, ParameterService parameterService) {
        this.licenseService = licenseService;
        this.parameterService = parameterService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

    // URLs que devem ser ignoradas pelo interceptor
    if (request.getRequestURI().equals("/licenses/expired-license.html") || 
        request.getRequestURI().equals("/parameters/error-date-param.html")) {
        return true;
    }

    //LicenseInfoDTO licenseInfo = licenseService.getLicenseEndDate();
    Parameter parameter = parameterService.getParameterById(1L);
    ValidateLicense validateLicense = new ValidateLicense(licenseService);

    if (parameter.getLastValidAccessDate() != null && 
        parameter.getLastValidAccessDate().isAfter(LocalDateTime.now())) {

        if (!response.isCommitted()) {
            response.sendRedirect(request.getContextPath() + "/parameters/error-date-param.html");
        }

        return false;
    }

    if (parameter.getEndDate() != null && 
        parameter.getEndDate().isBefore(LocalDateTime.now())) {

        if (!response.isCommitted()) {
            response.sendRedirect("/licenses/expired-license.html");
        }

        return false;
    }

    if(parameter.getLastCheckDate() != null && 
        parameter.getLastCheckDate().isBefore(LocalDateTime.now())) {
            if (!validateLicense.validate()) {
                if (!response.isCommitted()) {
                    response.sendRedirect("/licenses/expired-license.html");
                }
                return false;
            }
        return true;
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
