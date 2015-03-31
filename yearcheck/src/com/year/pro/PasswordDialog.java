package com.year.pro;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;

public class PasswordDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField jtpass;
	private String chars="abcdefghigkl";
	private Calendar calendar;
	private JTextField dtpass;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PasswordDialog dialog = new PasswordDialog();
			dialog.text();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void text(){
		calendar=Calendar.getInstance();
		calendar.setTime(new Date());
		int year=calendar.get(Calendar.YEAR)%100;
		int mon=calendar.get(Calendar.MONTH);
		int min=calendar.get(Calendar.DAY_OF_MONTH);
		System.out.println(min);
		int ap=calendar.get(Calendar.AM_PM);
		char[] cr=chars.toCharArray();
		String passd=""+year+cr[mon]+Integer.toOctalString(min)+(ap==0?"a":"p");
		System.out.println(passd);
	}

	/**
	 * Create the dialog.
	 */
	public PasswordDialog() {
		setTitle("\u8F93\u5165\u5BC6\u7801");
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		setBounds(100, 100, 300, 146);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel label = new JLabel("\u767B\u5F55\u5BC6\u7801:");
		label.setBounds(23, 13, 61, 15);
		contentPanel.add(label);
		
		jtpass = new JTextField();
		jtpass.setBounds(87, 10, 187, 21);
		contentPanel.add(jtpass);
		jtpass.setColumns(10);
		{
			JLabel label_1 = new JLabel("\u52A8\u6001\u5BC6\u7801\uFF1A");
			label_1.setBounds(23, 39, 61, 15);
			contentPanel.add(label_1);
		}
		{
			dtpass = new JTextField();
			dtpass.setBounds(87, 36, 187, 21);
			contentPanel.add(dtpass);
			dtpass.setColumns(10);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("\u786E\u5B9A");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						calendar=Calendar.getInstance();
						calendar.setTime(new Date());
						int year=calendar.get(Calendar.YEAR)%100;
						int mon=calendar.get(Calendar.MONTH);
						int min=calendar.get(Calendar.DAY_OF_MONTH);
						int ap=calendar.get(Calendar.AM_PM);
						char[] cr=chars.toCharArray();
						String passd=""+year+cr[mon]+Integer.toOctalString(min)+(ap==0?"a":"p");
						
						String pt=jtpass.getText();
						if("zxjy".equals(pt)&&dtpass.getText().equals(passd)){
							PasswordDialog.this.dispose();
						}else{
							JOptionPane.showMessageDialog(PasswordDialog.this, "√‹¬Î¥ÌŒÛ");
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("\u53D6\u6D88");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						System.exit(0);
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
	}
}
