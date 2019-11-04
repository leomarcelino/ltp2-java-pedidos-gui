package presenters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import dao.ClienteDAO;
import gui.clientes.ClienteFormFrame;
import gui.clientes.ClienteListFrame;
import models.Cliente;
import models.Produto;

public class ClientePresenter implements Presenter {
	
	private AppPresenter appPresenter;
	
	private ClienteListFrame listView;
	private ClienteFormFrame formView;
	
	private ClienteDAO dao;

	public ClientePresenter(AppPresenter appPresenter) {
		this.appPresenter = appPresenter;
		
		this.dao = new ClienteDAO();
		
		initComponents();
	}

	private void initComponents() {
		listView = new ClienteListFrame(this);
		formView = new ClienteFormFrame(this);
	}
	
	@Override
	public void execute() {
		appPresenter.openFrameInDesktop(listView);
	}

	public void openFormOnNewMode() {
		openForm(new Cliente());
	}

	public void openFormOnEditMode() {
		int r = listView.getSelectedRowIndex();

		if (r < 0 || r > listView.getTableRowCount()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		long id = listView.getId(r);

		Cliente cliente = dao.find(id);
		
		if (cliente == null) {
			JOptionPane.showMessageDialog(listView, "Registro não encontrado!");
			return;
		}
		
		openForm(cliente);
	}

	public void delete() {
		int r = listView.getSelectedRowIndex();
		
		if (r < 0 || r > listView.getTableRowCount()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		long id = listView.getId(r);

		Cliente cliente = dao.find(id);
		
		if (cliente == null) {
			JOptionPane.showMessageDialog(listView, "Registro não encontrado!");
			return;
		}

		int resp = JOptionPane.showConfirmDialog(listView, "Confirma exclusão do registro?", listView.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) {
			return;
		}
		
		dao.delete(cliente);

		search(null);
	}

	public void openForm(Cliente Cliente) {
		formView.clear();
		formView.setBean(Cliente);
		appPresenter.openFrameInDesktop(formView);
	}

	public void search(String search) {
		List<Cliente> clientes = dao.list(search);
		
		listView.setClientes(clientes);
	}

	public void save(Cliente cliente) {
		if (cliente.getId() == 0L) {
			dao.create(cliente);
			formView.clear();
		} else {
			dao.update(cliente);
			formView.dispose();
		}

		search(null);
	}
}