package com.picoto.test.domain;

public class TestDomain {

	private Integer id;
	
	private String descripcion;
	
	public TestDomain() {
		super();
	}

	public TestDomain(Integer id, String descripcion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public String toString() {
		return "Test (id=" + id + ", descripcion=" + descripcion + ")";
	}
	
	
	
}
