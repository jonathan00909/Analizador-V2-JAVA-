package Visual;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.table.DefaultTableModel;

public class Menu extends JFrame implements ActionListener {
    private JTextArea entradaField;
    private JButton traerCodigoFuenteButton;
    private JButton guardarButton;
    private JButton analizarButton;
    private JButton limpiarAnalizadorLexicoButton;
    private JButton analizarSintacticaButton;
    private JButton analizarSemanticaButton;
    private JTable tabla;
    private DefaultTableModel tablaModel;

    private JTable tabla2;
    private DefaultTableModel tablaModel2;

    public Menu() {
        setTitle("Analizador Léxico, Sintáctico y Semántico");
        setSize(1500, 630);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panelPrincipal = new JPanel(new BorderLayout());
        JPanel panelSuperior = new JPanel(new BorderLayout());

        JPanel codigoFuentePanel = new JPanel();
        traerCodigoFuenteButton = new JButton("Abrir fuente");
        traerCodigoFuenteButton.addActionListener(this);
        codigoFuentePanel.add(traerCodigoFuenteButton);
        panelSuperior.add(codigoFuentePanel, BorderLayout.WEST);

        guardarButton = new JButton("Guardar");
        panelSuperior.add(guardarButton, BorderLayout.EAST);

        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);

        entradaField = new JTextArea(10, 30);
        entradaField.setLineWrap(true);
        entradaField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(entradaField);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        tablaModel = new DefaultTableModel();
        tablaModel.addColumn("Lexema");
        tablaModel.addColumn("Token");
        tablaModel.addColumn("Número");
        tabla = new JTable(tablaModel);
        JScrollPane scrollPaneTabla = new JScrollPane(tabla);
        panelPrincipal.add(scrollPaneTabla, BorderLayout.WEST);

        tablaModel2 = new DefaultTableModel();
        tablaModel2.addColumn("Resultados semánticos");
        tabla2 = new JTable(tablaModel2);
        JScrollPane scrollPanesTabla = new JScrollPane(tabla2);
        panelPrincipal.add(scrollPanesTabla, BorderLayout.EAST);

        JPanel panelBotones = new JPanel(new BorderLayout());

        JPanel buttonPanelL = new JPanel(new FlowLayout(FlowLayout.LEFT));
        analizarButton = new JButton("Analizar léxicamente");
        buttonPanelL.add(analizarButton);
        panelBotones.add(buttonPanelL, BorderLayout.WEST);

        limpiarAnalizadorLexicoButton = new JButton("Borrar Tabla Léxica");
        buttonPanelL.add(limpiarAnalizadorLexicoButton);

        JPanel buttonPanelR = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        analizarSintacticaButton = new JButton("Analizar Sintácticamente");
        buttonPanelR.add(analizarSintacticaButton);
        analizarSemanticaButton = new JButton("Analizar Semánticamente");
        buttonPanelR.add(analizarSemanticaButton);
        panelBotones.add(buttonPanelR, BorderLayout.EAST);

        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == traerCodigoFuenteButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Archivos de texto (.txt)", "txt"));

            int returnValue = fileChooser.showOpenDialog(null);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    BufferedReader reader = new BufferedReader(new FileReader(selectedFile));
                    String line;
                    entradaField.setText(""); // Limpia el área de texto antes de cargar el nuevo archivo.
                    while ((line = reader.readLine()) != null) {
                        entradaField.append(line + "\n"); // Agrega el contenido del archivo al área de texto.
                    }
                    reader.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    

    public void showErrorDialog(String msg) {
        String errorMessage = msg;
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
