package presenters;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import gui.produtos.ProdutoFormFrame;
import gui.produtos.ProdutoListFrame;
import models.Produto;

public class ProdutoPresenter implements Presenter {
	
	private AppPresenter appPresenter;
	
	private ProdutoListFrame listView;
	private ProdutoFormFrame formView;
	
	List<Produto> produtos = new ArrayList<>();

	public ProdutoPresenter(AppPresenter appPresenter) {
		this.appPresenter = appPresenter;
		
		produtos = new ArrayList<>();
		
		produtos.add(new Produto(1L, "MONITOR", 899.9, 10.0));
		produtos.add(new Produto(2L, "TECLADO", 39.9, 20.0));
		produtos.add(new Produto(3L, "MOUSE", 19.9, 30.0));
		
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
		
		if (r < 0 || r > produtos.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}

		Produto produto = produtos.get(r);
		openForm(produto);
	}

	public void delete() {
		int r = listView.getSelectedRowIndex();
		
		if (r < 0 || r > produtos.size()) {
			JOptionPane.showMessageDialog(listView, "Nenhum registro selecionado!");
			return;
		}
		
		int resp = JOptionPane.showConfirmDialog(listView, "Confirma exclusão do registro?", listView.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) {
			return;
		}

		produtos.remove(r);
		search(null);
	}

	public void openForm(Produto produto) {
		formView.clear();
		formView.setBean(produto);
		appPresenter.openFrameInDesktop(formView);
	}

	public void search(String search) {
		listView.setProdutos(produtos);
	}

	public void save(Produto produto) {
		if (produto.getId() == 0L) {
			produto.setId(new Long(produtos.size() + 1));
			produtos.add(produto);
			formView.clear();
		} else {
			
			int index = produtos.indexOf(produto);
			produtos.set(index, produto);
			formView.dispose();
		}

		search(null);

	}
}