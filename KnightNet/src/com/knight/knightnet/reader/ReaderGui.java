package com.knight.knightnet.reader;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ReaderGui extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private final JButton btnTop = new JButton("");
	private final JButton btnMid = new JButton("");
	private final JButton btnBottom = new JButton("");
	private final JButton btnTopLeft = new JButton("");
	private final JButton btnTopRight = new JButton("");
	private final JButton btnBottomRight = new JButton("");
	private final JButton btnBottomLeft = new JButton("");
	private final JTextField txtInput = new JTextField();
	private final JButton btnTrain = new JButton("Train");
	private final JButton btnProcess = new JButton("Process");
	
	public boolean[] states = new boolean[7];
	private final JTextField txtOutput = new JTextField();
	Reader reader;

	/**
	 * Create the frame.
	 */
	public ReaderGui(Reader r) {
		reader = r;
		txtInput.setBounds(32, 146, 86, 20);
		txtInput.setColumns(10);
		initGUI();
	}
	private void initGUI() {
		setTitle("ANN Reader");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 169, 281);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		btnTop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				clickButton(0, btnTop);
			}
		});
		btnTop.setBounds(32, 11, 89, 14);
		
		panel.add(btnTop);
		btnMid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(3, btnMid);
			}
		});
		btnMid.setBounds(32, 66, 89, 14);
		
		panel.add(btnMid);
		btnBottom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(6, btnBottom);
			}
		});
		btnBottom.setBounds(32, 121, 89, 14);
		
		panel.add(btnBottom);
		btnTopLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(1, btnTopLeft);
			}
		});
		btnTopLeft.setBounds(20, 25, 16, 39);
		
		panel.add(btnTopLeft);
		btnTopRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(2, btnTopRight);
			}
		});
		btnTopRight.setBounds(112, 25, 16, 39);
		
		panel.add(btnTopRight);
		btnBottomRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(5, btnBottomRight);
			}
		});
		btnBottomRight.setBounds(112, 81, 16, 39);
		
		panel.add(btnBottomRight);
		btnBottomLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickButton(4, btnBottomLeft);
			}
		});
		btnBottomLeft.setBounds(20, 81, 16, 39);
		
		panel.add(btnBottomLeft);
		
		panel.add(txtInput);
		btnTrain.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
			}
		});
		btnTrain.setBounds(32, 193, 89, 23);
		
		panel.add(btnTrain);
		btnProcess.setBounds(32, 166, 89, 23);
		
		panel.add(btnProcess);
		txtOutput.setColumns(10);
		txtOutput.setBounds(32, 221, 86, 20);
		
		panel.add(txtOutput);
	}
	
	private void clickButton(int state, JButton b) {
		states[state] = !states[state];
		if(states[state]) {
			b.setText("-");
		} else {
			b.setText("");
		}
	}
}
