package presenters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gui.clientes.ClienteFormFrame;
import gui.clientes.ClienteListFrame;
import models.Cliente;

public class ClientePresenter implements Presenter {
	
	private AppPresenter appPresenter;
	
	private ClienteListFrame listView;
	private ClienteFormFrame formView;
	
	List<Cliente> clientes = new ArrayList<>();

	public ClientePresenter(AppPresenter appPresenter) {
		this.appPresenter = appPresenter;
		
		clientes = new ArrayList<>();
		
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
		
		if (r < 0 || r > clientes.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		Cliente Cliente = clientes.get(r);
		openForm(Cliente);
	}

	public void delete() {
		int r = listView.getSelectedRowIndex();
		
		if (r < 0 || r > clientes.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}
		
		int resp = JOptionPane.showConfirmDialog(listView, "Confirma exclusão do registro?", listView.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) {
			return;
		}

		clientes.remove(r);
		search(null);
	}

	public void openForm(Cliente Cliente) {
		formView.clear();
		formView.setBean(Cliente);
		appPresenter.openFrameInDesktop(formView);
	}

	public void search(String search) {
		listView.setClientes(clientes);
	}

	public void save(Cliente Cliente) {
		if (Cliente.getId() == 0L) {
			Cliente.setId(new Long(clientes.size() + 1));
			clientes.add(Cliente);
			formView.clear();
		} else {
			
			int index = clientes.indexOf(Cliente);
			clientes.set(index, Cliente);
			formView.dispose();
		}

		search(null);

	}
}