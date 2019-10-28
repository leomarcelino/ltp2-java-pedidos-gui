package gui.produtos;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import models.Produto;
import presenters.ProdutoPresenter;

public class ProdutoFormFrame extends JInternalFrame implements ActionListener {
	
	private static final long serialVersionUID = -3020565497715143857L;
	
	private JTextField idField;
	private JTextField descricaoField;

	private JFormattedTextField precoField;
	private JFormattedTextField estoqueField;
	
	private NumberFormat precoFormat;
    private NumberFormat estoqueFormat;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private ProdutoPresenter presenter;
	
	public ProdutoFormFrame(ProdutoPresenter presenter) {
		super("Produto", false, true);
		
		this.presenter = presenter;
		
		initComponents();
		
		buildGUI();
		
		clear();
	}
	
	private void initComponents() {
		addInternalFrameListener(new InternalFrameAdapter() {

			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				super.internalFrameClosing(e);
				cancelButton.doClick();
			}
		});

		setUpFormats();
		
		idField = new JTextField(6);
		idField.setEditable(false);
		
		descricaoField = new JTextField(80);
		
		precoField = new JFormattedTextField(precoFormat);
		precoField.setHorizontalAlignment(JTextField.RIGHT);
		
		estoqueField = new JFormattedTextField(estoqueFormat);
		estoqueField.setHorizontalAlignment(JTextField.RIGHT);
		
		saveButton = new JButton("Salvar");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		
		cancelButton = new JButton("Cancelar");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
	}
	
	private void setUpFormats() {
        precoFormat = NumberFormat.getNumberInstance();
        precoFormat.setMinimumFractionDigits(2);
 
        estoqueFormat = NumberFormat.getNumberInstance();
        estoqueFormat.setMinimumFractionDigits(3);
    }
	 
	private void buildGUI() {
		setLayout(new BorderLayout());
		add(buildFormPanel(), BorderLayout.CENTER);
		add(buildButtonsPanel(), BorderLayout.SOUTH);
		pack();
	}
	
	private JPanel buildButtonsPanel() {
		FormLayout layout = new FormLayout(
				"10dlu:g, 50dlu, 2dlu, 50dlu, 10dlu", // columns
		        "pref, 5dlu"); //rows

		//JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		JPanel panel = new JPanel(layout);

		CellConstraints cc = new CellConstraints();
		
		int row = 1;
		
		panel.add(saveButton, cc.xy(2, row));
		panel.add(cancelButton, cc.xy(4, row));
		
		return panel;
	}
	
	private JPanel buildFormPanel() {
		FormLayout layout = new FormLayout(
				"10dlu:g, pref, 2dlu, 50dlu, 20dlu, pref, 2dlu, 50dlu, 10dlu:g", // columns
		        "10dlu:g, pref, 2dlu, pref, 2dlu, pref, 10dlu:g"); //rows
		
		JPanel panel = new JPanel(layout);
		
		CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		panel.add(new JLabel("ID:"), cc.xy(2, row));
		panel.add(idField, cc.xy(4, row));
		
		row += 2;
		panel.add(new JLabel("Descrição:"), cc.xy(2, row));
		panel.add(descricaoField, cc.xyw(4, row, 5));

		row += 2;
		panel.add(new JLabel("Preço:"), cc.xy(2, row));
		panel.add(precoField, cc.xy(4, row));

		panel.add(new JLabel("Estoque:"), cc.xy(6, row));
		panel.add(estoqueField, cc.xy(8, row));

		return panel;
	}
	
	@Override
	public void requestFocus() {
		descricaoField.requestFocus();
	}
	
	private void save() {
		Produto produto = new Produto();
		
		if (!"<NOVO>".equals(idField.getText())) {
			produto.setId(new Long(idField.getText()));
		}
		
		produto.setDescricao(descricaoField.getText());
		produto.setPreco(Double.parseDouble(precoField.getText()));
		produto.setEstoque(Double.parseDouble(estoqueField.getText()));
		
		presenter.save(produto);
	}

	public void setBean(Produto produto) {
		if (produto.getId() > 0) {
			idField.setText(String.valueOf(produto.getId()));
		}
		
		descricaoField.setText(produto.getDescricao());
		precoField.setValue(produto.getPreco());
		estoqueField.setValue(produto.getEstoque());
	}
	
	public void clear() {
		idField.setText("<NOVO>");
		descricaoField.setText("");
		precoField.setValue(0.0);
		estoqueField.setValue(0.0);
		
		descricaoField.requestFocus();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Runnable r = new Runnable() {
			
			@Override
			public void run() {
				String command = e.getActionCommand();
				
				switch (command) {
				case "cancel":
					dispose();
					break;
				case "save":
					save();
					break;
				default:
					break;
				}
			}
		};
		
		SwingUtilities.invokeLater(r);
	}
}
