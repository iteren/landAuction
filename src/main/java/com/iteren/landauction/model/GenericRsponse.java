package com.iteren.landauction.model;

import lombok.Data;

@Data
public class GenericRsponse<T> {
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
		return response;
	}
}
