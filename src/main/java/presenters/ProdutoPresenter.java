package presenters;

import java.util.List;

import javax.swing.JOptionPane;

import dao.ProdutoDAO;
import gui.produtos.ProdutoFormFrame;
import gui.produtos.ProdutoListFrame;
import models.Produto;

public class ProdutoPresenter implements Presenter {
	
	private AppPresenter appPresenter;
	
	private ProdutoListFrame listView;
	private ProdutoFormFrame formView;
	
	private ProdutoDAO dao;

	public ProdutoPresenter(AppPresenter appPresenter) {
		this.appPresenter = appPresenter;
		
		this.dao = new ProdutoDAO();
		
		initComponents();
	}

	private void initComponents() {
		listView = new ProdutoListFrame(this);
		formView = new ProdutoFormFrame(this);
	}
	
	@Override
	public void execute() {
		appPresenter.openFrameInDesktop(listView);
	}

	public void openFormOnNewMode() {
		openForm(new Produto());
	}

	public void openFormOnEditMode() {
		int r = listView.getSelectedRowIndex();

		if (r < 0 || r > listView.getTableRowCount()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		long id = listView.getId(r);

		Produto produto = dao.find(id);
		
		if (produto == null) {
			JOptionPane.showMessageDialog(listView, "Registro não encontrado!");
			return;
		}
		
		openForm(produto);
	}

	public void delete() {
		int r = listView.getSelectedRowIndex();
		
		if (r < 0 || r > listView.getTableRowCount()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		long id = listView.getId(r);

		Produto produto = dao.find(id);
		
		if (produto == null) {
			JOptionPane.showMessageDialog(listView, "Registro não encontrado!");
			return;
		}

		int resp = JOptionPane.showConfirmDialog(listView, "Confirma exclusão do registro?", listView.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) {
			return;
		}
		
		dao.delete(produto);

		search(null);
	}

	public void openForm(Produto produto) {
		formView.clear();
		formView.setBean(produto);
		appPresenter.openFrameInDesktop(formView);
	}

	public void search(String search) {
		List<Produto> produtos = dao.list(search);
		
		listView.setProdutos(produtos);
	}

	public void save(Produto produto) {
		if (produto.getId() == 0L) {
			dao.create(produto);
			formView.clear();
		} else {
			dao.update(produto);
			formView.dispose();
		}

		search(null);
	}
}