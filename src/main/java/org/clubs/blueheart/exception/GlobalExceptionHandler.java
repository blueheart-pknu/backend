package org.clubs.blueheart.exception;

import org.clubs.blueheart.activity.domain.Activity;
import org.clubs.blueheart.activity.domain.ActivityHistory;
import org.clubs.blueheart.auth.api.AuthApi;
import org.clubs.blueheart.group.api.GroupApi;
import org.clubs.blueheart.notification.domain.Notification;
import org.clubs.blueheart.user.api.UserApi;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice(annotations = {RestController.class}, basePackageClasses = {Activity.class, ActivityHistory.class ,AuthApi.class, GroupApi.class, Notification.class, UserApi.class})
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handle ApiException
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<CustomExceptionStatus> handleApiException(ApiException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle ApplicationException
     */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<CustomExceptionStatus> handleApplicationException(ApplicationException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle DaoException
     */
    @ExceptionHandler(DaoException.class)
    public ResponseEntity<CustomExceptionStatus> handleDaoException(DaoException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);

    }/**
     * Handle DtoException
     */
    @ExceptionHandler(DtoException.class)
    public ResponseEntity<CustomExceptionStatus> handleDtoException(DtoException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle DomainException
     */
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<CustomExceptionStatus> handleDomainException(DomainException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle RepositoryException
     */
    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<CustomExceptionStatus> handleRepositoryException(RepositoryException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle UtilException
     */
    @ExceptionHandler(UtilException.class)
    public ResponseEntity<CustomExceptionStatus> handleUtilException(UtilException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle MiddlewareException
     */
    @ExceptionHandler(MiddlewareException.class)
    public ResponseEntity<CustomExceptionStatus> handleMiddlewareException(MiddlewareException ex, WebRequest request) {
        return buildErrorResponse(ex.getStatus(), request);
    }

    /**
     * Handle Generic Exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomExceptionStatus> handleGenericException(Exception ex, WebRequest request) {
        CustomExceptionStatus errorResponse = new CustomExceptionStatus(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Build the error response
     */
    private ResponseEntity<CustomExceptionStatus> buildErrorResponse(ExceptionStatus errorCode, WebRequest request) {
        CustomExceptionStatus errorResponse = new CustomExceptionStatus(
                errorCode.getStatusCode(),
                errorCode.getMessage(),
                errorCode.getError()
        );
        return ResponseEntity.status(errorCode.getStatusCode()).body(errorResponse);
    }
}