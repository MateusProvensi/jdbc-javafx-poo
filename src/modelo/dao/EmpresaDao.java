package modelo.dao;

import java.util.List;

import modelo.entidades.Empresa;

public interface EmpresaDao {

	void insert(Empresa obj);

	void update(Empresa obj);

	List<Empresa> acharTodos();

}
