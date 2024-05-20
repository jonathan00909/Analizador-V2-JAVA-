package Visual;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import Logic.Analyzer.Lexico.GrammarV1_0;
import Logic.Analyzer.Lexico.Token.Token;
//import Logic.Analyzer.Semantico.Semantic;
//import Logic.Analyzer.Sintactico.Exp;
import Logic.Analyzer.Sintactico.Syntatic;
//import Logic.Analyzer.Sintactico.ejemplo_sintactico_AST;
import Logic.Behavior.Analyzer;
import Logic.Behavior.Grammar;
import Logic.Formats.StringAnalyzer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.List;

public class Menu extends JFrame implements ActionListener {
    private JTextArea entradaField;
    private JButton analizarButton;
    private JButton analizarSintacticaButton;
    private JButton analizarSemanticaButton;
    private JButton traerCodigoFuenteButton;
    private JButton limpiarAnalizadorLexicoButton;
    private JButton guardarButton;
    private JTable tabla;
    private DefaultTableModel tablaModel;

    private JTable tabla2;
    private DefaultTableModel tablaModel2;
    //private JFrame frameSemantico; 

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
        guardarButton.addActionListener(this);
        panelSuperior.add(guardarButton, BorderLayout.EAST);
        
        panelPrincipal.add(panelSuperior, BorderLayout.NORTH);
/*------------------------------------------------------------------ */
        // Panel de captura en la esquina superior izquierda
        entradaField = new JTextArea(10, 30);
        entradaField.setLineWrap(true);
        entradaField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(entradaField);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);
/*------------------------------------------------------------------ */
        tablaModel = new DefaultTableModel();
        tablaModel.addColumn("Lexema");
        tablaModel.addColumn("Token");
        tablaModel.addColumn("Número");
        tabla = new JTable(tablaModel);

        JScrollPane scrollPaneTabla = new JScrollPane(tabla);
        panelPrincipal.add(scrollPaneTabla, BorderLayout.WEST);
//-------------------------------------------------------------------
        tablaModel2 = new DefaultTableModel();
        tablaModel2.addColumn("Resultados semánticos");
        tabla2 = new JTable(tablaModel2);

        JScrollPane scrollPanesTabla = new JScrollPane(tabla2);
        panelPrincipal.add(scrollPanesTabla, BorderLayout.EAST);

        //tabla2.getTableHeader().setBackground(new Color(168, 169, 240));
        //tabla2.setSelectionBackground(Color.red);
        //JScrollPane scrollPaneTabla = new JScrollPane(tabla);


        //resultadoSemanticoArea.setEditable(false); // Para que el usuario no pueda editar el resultado
        //frameSemantico = new JFrame("Resultado Semántico");
        //frameSemantico.setSize(400, 300);
        //frameSemantico.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo la nueva pestaña al cerrarla

        JPanel panelBotones = new JPanel(new BorderLayout());

        JPanel buttonPanelL = new JPanel(new FlowLayout(FlowLayout.LEFT));

        analizarButton = new JButton("Analizar léxicamente");
        analizarButton.addActionListener(this);
        buttonPanelL.add(analizarButton);

        panelBotones.add(buttonPanelL, BorderLayout.WEST);

        limpiarAnalizadorLexicoButton = new JButton("Borrar Tabla Léxica");
        limpiarAnalizadorLexicoButton.addActionListener(this);
        buttonPanelL.add(limpiarAnalizadorLexicoButton);

        JPanel buttonPanelR = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        analizarSintacticaButton = new JButton("Analizar Sintácticamente");
        analizarSintacticaButton.addActionListener(this);
        buttonPanelR.add(analizarSintacticaButton);

        analizarSemanticaButton = new JButton("Analizar Semánticamente");
        analizarSemanticaButton.addActionListener(this);
        buttonPanelR.add(analizarSemanticaButton);

        panelBotones.add(buttonPanelR, BorderLayout.EAST);
        panelPrincipal.add(panelBotones, BorderLayout.SOUTH);

        getContentPane().add(panelPrincipal);
        setVisible(true);

        //-----------------------------------------------------------

        //JPanel panelSemantico = new JPanel(new BorderLayout());
        //JScrollPane scrollPaneSemantico = new JScrollPane(tabla2);
        //panelSemantico.add(scrollPaneSemantico, BorderLayout.CENTER);

        //frameSemantico.add(panelSemantico);
        //frameSemantico.setVisible(false);  // Inicialmente ocultar la nueva pestaña

        //-----------------------------------------------------------
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Analyzer analyzer;
        analyzer = new StringAnalyzer();
        GrammarV1_0 grammar = new GrammarV1_0();
        Grammar grammarToAnalize = grammar;
        Syntatic analizarSintact = new Syntatic();

        String entrada = entradaField.getText();
        analyzer.analyze(entrada, grammarToAnalize);
        String command = e.getActionCommand();
        List<Token> listaTokens = grammar.getLexycal();
        boolean isSyntaticCorrect = analizarSintact.isValid(listaTokens);

        System.out.println(command);
        if(command.equalsIgnoreCase("Borrar Tabla Léxica") || command.equalsIgnoreCase("Analizar léxicamente")){
            deleteColumns();
        }
        
        
        if (command.equalsIgnoreCase("Analizar léxicamente")) {
            for (Token index : grammar.getLexycal()) {
                tablaModel.addRow(new Object[] { index.getLexema().getSubString(), index.getId().getDescription(), index.getId().getId() });
                //System.out.println(index);
            }
        }
        
        if(command.equalsIgnoreCase("Abrir fuente")){
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
                
        if(command.equalsIgnoreCase("Analizar Sintácticamente")){
            System.out.println("clic en analizador sintactico");
            
            if(!isSyntaticCorrect){
                JOptionPane.showMessageDialog(null, analizarSintact.getStateMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null, analizarSintact.getStateMessage(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if(command.equalsIgnoreCase("Analizar Semánticamente")){
            //frameSemantico.setVisible(false);
            while((tablaModel2.getRowCount()>0)){
                tablaModel2.removeRow(tablaModel2.getRowCount()-1);
            }
            if(!analizarSintact.getErrorSemantic().isEmpty()){
                for (String elemento : analizarSintact.getErrorSemantic()) {
                    tablaModel2.addRow(new Object[] {elemento});
                }
            }else{
                tablaModel2.addRow(new Object[] {"No hubo errores semanticos"});
            }
            //frameSemantico.setVisible(true);

        }
    }


    private void deleteColumns(){
        while((tablaModel.getRowCount()>0)){
            tablaModel.removeRow(tablaModel.getRowCount()-1);
        }
            
    }

    public void showErrorDialog(String msg) {
        String errorMessage = msg;
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
