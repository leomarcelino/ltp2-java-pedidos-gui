package gui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import presenters.AppPresenter;

public class MainFrame extends JFrame implements ActionListener {
	
	private static final long serialVersionUID = -2789386886180302080L;

	private JDesktopPane desktopPane;
	
	private JMenuBar menuBar;
	
	private List<String> opened = new ArrayList<>();
	
	private AppPresenter controller;
	
	public MainFrame(AppPresenter controller) {
		super("ADS SYSPED 1.0");
		
		this.controller = controller;
		
		initComponents();
		
		buildGUI();
	}

	private void initComponents() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		desktopPane = new JDesktopPane();
		
		menuBar = new JMenuBar();
		
		buildMenu();
	}
	
	private void buildMenu() {
		JMenu menu = new JMenu("Sistema");
		
		JMenuItem menuItem;
		
		menuItem = new JMenuItem("Produtos");
		menuItem.setActionCommand("produtos");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuItem = new JMenuItem("Clientes");
		menuItem.setActionCommand("clientes");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menuItem = new JMenuItem("Pedidos");
		menuItem.setActionCommand("pedidos");
		menuItem.addActionListener(this);
		menu.add(menuItem);

		menu.addSeparator();

		menuItem = new JMenuItem("Sair");
		menuItem.setActionCommand("sair");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		
		menuBar.add(menu);
	}
	
	private void buildGUI() {
		setLayout(new BorderLayout());
		setJMenuBar(menuBar);
		setExtendedState(JFrame.MAXIMIZED_BOTH);

		add(desktopPane);
	}
	
	@Override
	public void actionPerformed(final ActionEvent e) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				String command = e.getActionCommand();
				
				switch (command) {
				case "sair":
					controller.finishApplication();
					break;
				default:
					controller.executePresenter(command);
					break;
				}
			}
		};
		
		SwingUtilities.invokeLater(r);
	}

	public void openFrame(JInternalFrame frame) {
		if (!opened.contains(frame.getTitle())) {
			frame.addInternalFrameListener(new InternalFrameAdapter() {

				@Override
				public void internalFrameOpened(InternalFrameEvent e) {
					super.internalFrameOpened(e);
					opened.add(frame.getTitle());
					e.getInternalFrame().requestFocus();
				}

				@Override
				public void internalFrameClosed(InternalFrameEvent e) {
					super.internalFrameClosed(e);
					opened.remove(e.getInternalFrame().getTitle());
					desktopPane.remove(e.getInternalFrame());
				}
			});

			desktopPane.add(frame);
			center(frame);
			frame.setVisible(true);
		}
		
		frame.toFront();
		frame.requestFocus();
	}
	
	private void center(JInternalFrame frame) {
		Dimension desktopSize = desktopPane.getSize();
		Dimension frameSize = frame.getSize();
		int x = (desktopSize.width - frameSize.width) / 2;
		int y = (desktopSize.height - frameSize.height) / 2;
		
		frame.setLocation(x, y);
	}
}