package presenters;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import gui.main.MainFrame;

public class AppPresenter implements Presenter {

	private MainFrame view;
	
	private Map<String, Presenter> presenters;
	
	public AppPresenter() {
		view = new MainFrame(this);
		
		initPresenters();
	}
	
	private void initPresenters() {
		presenters = new HashMap<>();
		presenters.put("produtos", new ProdutoPresenter(this));
		presenters.put("clientes", new ClientePresenter(this));
	}
	
	@Override
	public void execute() {
		view.setVisible(true);
	}

	public void finishApplication() {
		int resp = JOptionPane.showConfirmDialog(view, "Deseja fechar aplicação?", view.getTitle(), JOptionPane.YES_NO_OPTION);
		
		if (resp == JOptionPane.NO_OPTION) return;
		
		System.exit(0);
	}

	public void executePresenter(String command) {
		Presenter presenter = presenters.get(command);
		
		if (presenter == null) {
			JOptionPane.showMessageDialog(view, "Função não encontrada", view.getTitle(), JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		presenter.execute();
	}
	
	public void openFrameInDesktop(JInternalFrame frame) {
		view.openFrame(frame);
	}
}