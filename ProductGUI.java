package product_management_gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Optional;

public class ProductGUI extends JFrame {
    private ProductManager productManager;

    private JTextField idField, nameField, priceField, quantityField;
    private JButton addButton, updateButton, deleteButton, listButton;
    private JTable productTable;
    private DefaultTableModel tableModel;

    public ProductGUI() {
        productManager = new ProductManager();

        setTitle("Gerenciamento de Produtos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Painel de entrada
        JPanel inputPanel = new JPanel(new GridLayout(5, 2));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Dados do Produto"));

        inputPanel.add(new JLabel("ID (para Atualizar/Excluir):"));
        idField = new JTextField();
        idField.setEditable(false); // ID é gerado automaticamente, só para visualização ou busca
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Nome:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Preço:"));
        priceField = new JTextField();
        inputPanel.add(priceField);

        inputPanel.add(new JLabel("Quantidade:"));
        quantityField = new JTextField();
        inputPanel.add(quantityField);

        add(inputPanel, BorderLayout.NORTH);

        // Painel de botões
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Adicionar");
        updateButton = new JButton("Atualizar");
        deleteButton = new JButton("Excluir");
        listButton = new JButton("Listar Todos");

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(listButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Tabela de exibição
        String[] columnNames = {"ID", "Nome", "Preço", "Quantidade"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(productTable);
        add(scrollPane, BorderLayout.CENTER);

        // Ações dos botões
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O nome do produto não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    productManager.addProduct(name, price, quantity);
                    JOptionPane.showMessageDialog(null, "Produto adicionado com sucesso!");
                    clearFields();
                    listProducts();
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "Preço e Quantidade devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao adicionar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String name = nameField.getText();
                    double price = Double.parseDouble(priceField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());

                    if (name.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "O nome do produto não pode ser vazio.", "Erro", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (productManager.updateProduct(id, name, price, quantity)) {
                        JOptionPane.showMessageDialog(null, "Produto atualizado com sucesso!");
                        clearFields();
                        listProducts();
                    } else {
                        JOptionPane.showMessageDialog(null, "Produto com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID, Preço e Quantidade devem ser números válidos.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao atualizar produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    if (productManager.deleteProduct(id)) {
                        JOptionPane.showMessageDialog(null, "Produto excluído com sucesso!");
                        clearFields();
                        listProducts();
                    } else {
                        JOptionPane.showMessageDialog(null, "Produto com ID " + id + " não encontrado.", "Erro", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "ID deve ser um número válido.", "Erro", JOptionPane.ERROR_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro ao excluir produto: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        listButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listProducts();
            }
        });

        // Listener para seleção de linha na tabela
        productTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting() && productTable.getSelectedRow() != -1) {
                int selectedRow = productTable.getSelectedRow();
                idField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                nameField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                priceField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                quantityField.setText(tableModel.getValueAt(selectedRow, 3).toString());
            }
        });

        listProducts(); 
        setVisible(true);
    }

    private void listProducts() {
        tableModel.setRowCount(0); // Limpa a tabela
        List<Product> products = productManager.getAllProducts();
        for (Product product : products) {
            tableModel.addRow(new Object[]{product.getId(), product.getName(), String.format("%.2f", product.getPrice()), product.getQuantity()});
        }
    }

    private void clearFields() {
        idField.setText("");
        nameField.setText("");
        priceField.setText("");
        quantityField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new ProductGUI();
            }
        });
    }
}

