package gui.clientes;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;

import models.Cliente;
import models.TipoPessoa;
import presenters.ClientePresenter;

public class ClienteFormFrame extends JInternalFrame implements ActionListener {
	
	private static final long serialVersionUID = -3020565497715143857L;
	
	private JLabel cnpjCpfLabel = new JLabel("CPF:");
	
	private JTextField idField;
	private JTextField nomeField;
	private JTextField cnpjCpfField;
	
	private JComboBox<TipoPessoa> tipoPessoaCombo;
    
    private JButton saveButton;
    private JButton cancelButton;
    
    private ClientePresenter presenter;
	
	public ClienteFormFrame(ClientePresenter presenter) {
		super("Cliente", false, true);
		
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

		idField = new JTextField(6);
		idField.setEditable(false);
		
		nomeField = new JTextField(80);
		cnpjCpfField = new JTextField(18);
		
		tipoPessoaCombo = new JComboBox<>(new DefaultComboBoxModel<>(TipoPessoa.values()));
		tipoPessoaCombo.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if (tipoPessoaCombo.getSelectedItem() == TipoPessoa.FISICA) {
					cnpjCpfLabel.setText("CPF:");
				} else {
					cnpjCpfLabel.setText("CNPJ:");
				}
			}
		});
		
		saveButton = new JButton("Salvar");
		saveButton.setActionCommand("save");
		saveButton.addActionListener(this);
		
		cancelButton = new JButton("Cancelar");
		cancelButton.setActionCommand("cancel");
		cancelButton.addActionListener(this);
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
				"10dlu:g, pref, 2dlu, 50dlu, 10dlu, 20dlu, pref, 2dlu, 80dlu, 10dlu:g", // columns
		        "10dlu:g, pref, 2dlu, pref, 2dlu, pref, 10dlu:g"); //rows
		
		JPanel panel = new JPanel(layout);
		
		CellConstraints cc = new CellConstraints();
		
		int row = 2;
		
		panel.add(new JLabel("ID:"), cc.xy(2, row));
		panel.add(idField, cc.xy(4, row));
		
		row += 2;
		panel.add(new JLabel("Nome:"), cc.xy(2, row));
		panel.add(nomeField, cc.xyw(4, row, 6));

		row += 2;
		panel.add(new JLabel("Tipo:"), cc.xy(2, row));
		panel.add(tipoPessoaCombo, cc.xyw(4, row, 2));

		panel.add(cnpjCpfLabel, cc.xy(7, row));
		panel.add(cnpjCpfField, cc.xy(9, row));

		return panel;
	}
	
	@Override
	public void requestFocus() {
		nomeField.requestFocus();
	}
	
	private void save() {
		Cliente cliente = new Cliente();
		
		if (!"<NOVO>".equals(idField.getText())) {
			cliente.setId(new Long(idField.getText()));
		}
		
		cliente.setNome(nomeField.getText());
		cliente.setTipo((TipoPessoa)tipoPessoaCombo.getSelectedItem());
		cliente.setCnpjCpf(cnpjCpfField.getText());
		
		presenter.save(cliente);
	}

	public void setBean(Cliente cliente) {
		if (cliente.getId() > 0) {
			idField.setText(String.valueOf(cliente.getId()));
		}
		
		nomeField.setText(cliente.getNome());
		tipoPessoaCombo.setSelectedItem(cliente.getTipo());
		cnpjCpfField.setText(cliente.getCnpjCpf());
	}
	
	public void clear() {
		idField.setText("<NOVO>");
		nomeField.setText("");
		tipoPessoaCombo.setSelectedIndex(0);
		cnpjCpfField.setText("");
		
		nomeField.requestFocus();
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
