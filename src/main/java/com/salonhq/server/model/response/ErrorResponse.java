package com.salonhq.server.model.response;

import lombok.Data;

@Data
public class ErrorResponse {

	private final Integer code;
	private final String message;

	public ErrorResponse(Integer code, String message) {
		this.code = code;
		this.message = message;
	}

}
