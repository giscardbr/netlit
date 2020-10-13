package netlit.payments.infra.error

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val result = ex.bindingResult

        val fields = result.fieldErrors.asSequence().map { "${it.objectName}, ${it.defaultMessage}" }
        val globals = result.globalErrors.asSequence().map { "${it.objectName}, ${it.defaultMessage}" }
        val errors = (fields + globals).toList()

        return ResponseEntity(InvalidArgumentResponse(errors), headers, status)
    }
}

