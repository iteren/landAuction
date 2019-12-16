package com.iteren.landauction.model;

import lombok.Data;

@Data
public class GenericRsponse<T> {
	private boolean success = true;
	private T data;
	private AppError error;
	
	public static <T> GenericRsponse<T> of(T data) {
		GenericRsponse<T> response = new GenericRsponse<>();
		response.setData(data);
		return response;
	}
	
	@SuppressWarnings("rawtypes")
	public static GenericRsponse failure(String error) {
		GenericRsponse response = new GenericRsponse();
		AppError e = new AppError();
		e.setMessage(error);
		response.setError(e);
		response.setSuccess(false);
		return response;
	}
	
	public static <T> GenericRsponse<T> failureOf(T data, String action, String error) {
		GenericRsponse<T> response = of(data);
		response.setSuccess(false);
		AppError e = new AppError();
		e.setMessage(error);
		e.setAction(action);
		response.setError(e);
		return response;
	}
}
