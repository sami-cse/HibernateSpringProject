package com.sami.application;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.table.*;

import org.hibernate.*;
import org.hibernate.cfg.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sami.model.User;



public class UserApplication extends JFrame{
    private JTable table;
    private JButton btnAdd;
    private DefaultTableModel tableModel;
    private JTextField txtField1;
    private JTextField txtField2;
    
    ApplicationContext ctx = new ClassPathXmlApplicationContext("SpringBeans.xml");
    User user = (User) ctx.getBean("user");

    private UserApplication() {
        createGUI();
    }

    private void createGUI() {
        setLayout(new BorderLayout());
        
        JScrollPane pane = new JScrollPane();
        
        table = new JTable();
        
        pane.setViewportView(table);
        
        JPanel eastPanel = new JPanel();
        
        btnAdd = new JButton("Add");
        
        eastPanel.add(btnAdd);
        
        JPanel northPanel = new JPanel();
        
        txtField1 = new JTextField();
        txtField2 = new JTextField();
        
        JLabel lblField1 = new JLabel("First Name");
        JLabel lblField2 = new JLabel("Last Name");
        
        northPanel.add(lblField1);
        northPanel.add(txtField1);
        northPanel.add(lblField2);
        northPanel.add(txtField2);
        
        txtField1.setPreferredSize(lblField1.getPreferredSize());
        txtField2.setPreferredSize(lblField2.getPreferredSize());

        add(northPanel, BorderLayout.NORTH);
        add(eastPanel, BorderLayout.EAST);
        add(pane,BorderLayout.CENTER);
        
        tableModel = new DefaultTableModel(new Object[]{"First Name","Last Name"},0);
        
        table.setModel(tableModel);
        
		Session session = sessionFactory().openSession();
		session.beginTransaction();
		Query query = session.createQuery("from User");
		
		List<User> list = query.list();
		
		for(User user1:list){
			String fName = user1.getFirstName();
			String lName = user1.getLastName();
			System.out.println("First Name: "+fName);
			System.out.println("Last Name: "+lName);
			
            tableModel.addRow(new Object[]{fName, lName});
		}
		
		session.getTransaction().commit();
		
        btnAdd.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                int count = tableModel.getRowCount()+1;
                tableModel.addRow(new Object[]{txtField1.getText(),txtField2.getText()});
                
    	    	user.setFirstName(txtField1.getText());
    	    	user.setLastName(txtField2.getText());
    		
    			Session session = sessionFactory().openSession();
    			
    			session.beginTransaction();
    			session.save(user);
    			session.getTransaction().commit();
            }
        });
    }
    
	public static SessionFactory sessionFactory(){
		return new Configuration().configure().buildSessionFactory();
	}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                UserApplication u1 = new UserApplication();
                u1.setTitle("Employee Records");
                u1.setLocationByPlatform(true);
                u1.pack();
                u1.setDefaultCloseOperation(EXIT_ON_CLOSE);
                u1.setVisible(true);
            }
        });
    }
} 