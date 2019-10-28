package presenters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gui.clientes.ClienteFormFrame;
import gui.clientes.ClienteListFrame;
import models.Cliente;
import models.TipoPessoa;

public class ClientePresenter implements Presenter {
	
	private AppPresenter appPresenter;
	
	private ClienteListFrame listView;
	private ClienteFormFrame formView;
	
	List<Cliente> Clientes = new ArrayList<>();

	public ClientePresenter(AppPresenter appPresenter) {
		this.appPresenter = appPresenter;
		
		Clientes = new ArrayList<>();
		
		Clientes.add(new Cliente(1L, "João da Silva", TipoPessoa.FISICA, "123.345.789-10"));
		Clientes.add(new Cliente(2L, "UNIFEOB", TipoPessoa.JURIDICA, "12.345.789/0001-01"));
		
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
		
		if (r < 0 || r > Clientes.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		Cliente Cliente = Clientes.get(r);
		openForm(Cliente);
	}

	public void delete() {
		int r = listView.getSelectedRowIndex();
		
		if (r < 0 || r > Clientes.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}
		
		int resp = JOptionPane.showConfirmDialog(listView, "Confirma exclusão do registro?", listView.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) {
			return;
		}

		Clientes.remove(r);
		search(null);
	}

	public void openForm(Cliente Cliente) {
		formView.clear();
		formView.setBean(Cliente);
		appPresenter.openFrameInDesktop(formView);
	}

	public void search(String search) {
		listView.setClientes(Clientes);
	}

	public void save(Cliente Cliente) {
		if (Cliente.getId() == 0L) {
			Cliente.setId(new Long(Clientes.size() + 1));
			Clientes.add(Cliente);
			formView.clear();
		} else {
			
			int index = Clientes.indexOf(Cliente);
			Clientes.set(index, Cliente);
			formView.dispose();
		}

		search(null);

	}
}