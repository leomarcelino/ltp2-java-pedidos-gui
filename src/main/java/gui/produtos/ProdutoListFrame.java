package gui.produtos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import gui.utils.ImageUtil;
import models.Produto;
import presenters.ProdutoPresenter;

public class ProdutoListFrame extends JInternalFrame implements ActionListener {

	private static final long serialVersionUID = -2637438284719902679L;
	
	private final String COLUMN_NAMES[] = new String[] {"ID", "Descrição", "Preço", "Estoque"};
	private final Integer COLUMN_WIDTHS[] = new Integer[] {40, 300, 120, 120};
	
	private ProdutoPresenter presenter;
	
	private JTable table;
	
	private DefaultTableModel tableModel;
	
	private JTextField searchField;
	
	private JButton searchButton;
	private JButton addButton;
	private JButton editButton;
	private JButton deleteButton;
	private JButton closeButton;
	
	private NumberFormat precoFormat;
	private NumberFormat estoqueFormat;
	
	public ProdutoListFrame(ProdutoPresenter presenter) {
		super("Produtos", false, true);
		
		this.presenter = presenter;
		
		initComponents();
		
		buildGUI();
	}
	
	private void initComponents() {
		addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				closeButton.doClick();
			}
		});
		
		initTable();
		
		setUpFormats();
		
		searchField = new JTextField(60);
		
		searchButton = new JButton(ImageUtil.loadIcon("search.png"));
		searchButton.setActionCommand("search");
		searchButton.addActionListener(this);
		
		addButton = new JButton("Novo");
		addButton.setActionCommand("new");
		addButton.addActionListener(this);
		
		editButton = new JButton("Alterar");
		editButton.setActionCommand("edit");
		editButton.addActionListener(this);

		deleteButton = new JButton("Excluir");
		deleteButton.setActionCommand("delete");
		deleteButton.addActionListener(this);
		
		closeButton = new JButton("Fechar");
		closeButton.setActionCommand("close");
		closeButton.addActionListener(this);
	}
	
	private void setUpFormats() {
        precoFormat = NumberFormat.getNumberInstance();
        precoFormat.setMinimumFractionDigits(2);
 
        estoqueFormat = NumberFormat.getNumberInstance();
        estoqueFormat.setMinimumFractionDigits(3);
    }

	private void initTable() {
		tableModel = new DefaultTableModel(COLUMN_NAMES, 0);
		table = new JTable(tableModel);
        table.setFillsViewportHeight(true);

		for (int c = 0; c < COLUMN_WIDTHS.length; c++) {
			TableColumn column = table.getColumnModel().getColumn(c);
			column.setMinWidth(COLUMN_WIDTHS[c]);
            column.setPreferredWidth(COLUMN_WIDTHS[c]);
		}
	}
	
	private void buildGUI() {
		setLayout(new BorderLayout());
		add(buildListPanel(), BorderLayout.CENTER);
		add(buildButtonsPanel(), BorderLayout.SOUTH);
		pack();
	}
	
	private JPanel buildButtonsPanel() {
		FormLayout layout = new FormLayout(
				"10dlu:g, 50dlu, 2dlu, 50dlu, 2dlu, 50dlu, 10dlu", // columns
		        "pref, 5dlu"); //rows

		//JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel panel = new JPanel(layout);

		CellConstraints cc = new CellConstraints();
		
		int row = 1;
		
		panel.add(addButton, cc.xy(2, row));
		panel.add(editButton, cc.xy(4, row));
		panel.add(deleteButton, cc.xy(6, row));
		
		return panel;
	}
	
	private JPanel buildListPanel() {
		FormLayout layout = new FormLayout(
				"10dlu, pref, 2dlu, 150dlu:g, 2dlu, 20dlu, 100dlu, 10dlu", // columns
		        "10dlu, pref, 2dlu, 150dlu:g, 10dlu"); //rows
		
		JPanel panel = new JPanel(layout);
		
		CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		panel.add(new JLabel("Procurar:"), cc.xy(2, row));
		panel.add(searchField, cc.xy(4, row));
		panel.add(searchButton, cc.xy(6, row));
		
		row += 2;
		
		panel.add(new JScrollPane(table), cc.xyw(2, row, 6));

		return panel;
	}
	

	public void setProdutos(List<Produto> produtos) {
		tableModel.setRowCount(0);
		

		for (Produto produto: produtos) {
			addProduto(produto);
		}
	}

	public void addProduto(Produto produto) {
		tableModel.addRow(new Object[] {
			produto.getId(),
			produto.getDescricao(),
			precoFormat.format(produto.getPreco()),
			estoqueFormat.format(produto.getEstoque())
		});
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				String command = e.getActionCommand();
				
				switch (command) {
				case "close":
					dispose();
					break;
				case "search":
					presenter.search(searchField.getText());
					break;
				case "new":
					presenter.openFormOnNewMode();
					break;
				case "edit":
					presenter.openFormOnEditMode();
					break;
				case "delete":
					presenter.delete();
					break;
				default:
					break;
				}
			}
		};
		
		SwingUtilities.invokeLater(r);		
	}

	public int getSelectedRowIndex() {
		return table.getSelectedRow();
	}
}
