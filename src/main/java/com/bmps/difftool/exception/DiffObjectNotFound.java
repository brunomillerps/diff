package com.bmps.difftool.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "An unknown ID was provided")
public class DiffObjectNotFound extends RuntimeException {
}
