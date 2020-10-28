package modelo.dao;

import java.util.List;

import modelo.entidades.Empresa;

public interface EmpresaDao {

	void insert(Empresa obj);

	void update(Empresa obj);
	
	void deletePeloId(Integer id);

	List<Empresa> acharTodos();

}
