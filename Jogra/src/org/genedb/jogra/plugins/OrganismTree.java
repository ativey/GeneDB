/*
 * Copyright (c) 2009 Genome Research Limited.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Library General Public License as published by the Free
 * Software Foundation; either version 2 of the License or (at your option) any
 * later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Library General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Library General Public License
 * along with this program; see the file COPYING.LIB. If not, write to the Free
 * Software Foundation Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307
 * USA
 */

package org.genedb.jogra.plugins;

import org.genedb.top.db.taxon.TaxonNode;
import org.genedb.top.db.taxon.TaxonNodeManager;
import org.genedb.jogra.drawing.Jogra;
import org.genedb.jogra.drawing.JograPlugin;
import org.genedb.jogra.services.NamedVector;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.bushe.swing.event.EventBus;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.CheckboxTree;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingEvent;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingListener;
import it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingModel;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.WindowConstants;
import javax.swing.tree.TreePath;

/**
 * Organism-Tree/Organism-Chooser plugin for Jogra. Displays the organisms in the database in a hierarchical view and allows
 * the user to select as many as necessary. This selection is then published on the EventBus for other plug-ins that need it,
 * like the Term Rationaliser. We make use of the TaxonNodeManager class from genedb/db to extract the tree of organisms.
 * Started: April 2009
 *
 * @author nds
 */
public class OrganismTree implements JograPlugin {
    private TaxonNodeManager taxonNodeManager;
    private List<String> userSelection = new ArrayList<String>(); //Stores the organism names that the user selects
    private Jogra jogra;

    private static final Logger logger = LoggerFactory.getLogger(OrganismTree.class);

    public void setTaxonNodeManager(TaxonNodeManager taxonNodeManager) {
        this.taxonNodeManager = taxonNodeManager;
    }

    /**
     * Supply a JPanel which will be displayed in the main Jogra application panel,
     * used for launching a plug-in, or displaying status
     *
     * @return a JPanel, ready for displaying
     */
    public JPanel getMainWindowPlugin(){
        final JPanel panel = new JPanel();
        final JLabel label = new JLabel("Select organism(s) to work with");
        final JButton button = new JButton("Load organism tree");
        //When user clicks button, load a new frame with Jtree
        ActionListener actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                try{
                    new SwingWorker<JFrame, Void>() {
                        @Override
                        protected JFrame doInBackground() throws Exception {
                               return getMainPanel();
                        }
                    }.execute();
                }catch(Exception e){ //handle exceptions better later
                    logger.debug(e);
                }
             }
        };
        button.addActionListener(actionListener);
        Box horizontalBox = Box.createHorizontalBox();
        horizontalBox.add(label);
        horizontalBox.add(button);
        horizontalBox.add(Box.createHorizontalGlue());
        panel.add(horizontalBox);
        return panel;
    }

    /**
     * Method that creates the JFrame object containing the tree of organisms
     * @return
     */
    public JFrame getMainPanel() {
        final JFrame frame = new JFrame("Organisms currently in the database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final JButton button = new JButton("Select");   //Create button to confirm user selection. Disabled at first

        //TaxonNodeManager created and initialised at start up. Create JTree
        TaxonNode taxonNode = taxonNodeManager.getTaxonNodeForLabel("Root");
        Vector orgTree = getOrganismTree(taxonNode);
        JTree tree = new JTree(orgTree);
        final CheckboxTree checkboxTree = new CheckboxTree(tree.getModel()); //checkboxnode constructor takes a tree model
        checkboxTree.getCheckingModel().setCheckingMode(TreeCheckingModel.CheckingMode.PROPAGATE_PRESERVING_CHECK); //Check if this can be both SINGLE & PROPOGATE
        checkboxTree.addTreeCheckingListener(new TreeCheckingListener() {
            public void valueChanged(TreeCheckingEvent e) {
                if(e.isCheckedPath()){
                    button.setText("Select");
                    button.setEnabled(true);
                }
            }
        });

        button.setEnabled(false);
        ActionListener actionListener = new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){

                try{
                    userSelection.clear(); //Clear any previous selections
                    TreePath tp[] = checkboxTree.getCheckingRoots();
                    for(TreePath p: tp){
                        userSelection.add(p.getLastPathComponent().toString());
                    }
                    frame.setVisible(false); //Make frame disappear
                    //frame.dispose(); //Or should frame be disposed??
                   
                    EventBus.publish("selection", userSelection); //EventBus publish mechanism here needs to be tested later
                    logger.info("ORGANISM TREE: User selected " + userSelection.toString());
                }catch(Exception e){ //handle exceptions better
                    logger.debug(e);
                    e.printStackTrace();
                }
             }
        };
        button.addActionListener(actionListener);
        //Place Jtree in scrollable pane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(checkboxTree);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.add(button, BorderLayout.PAGE_END);
        frame.setSize(500,500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        return frame;
    }

    /**
     * A recursive helper method to return the organism hierarchy in vector form
     *
     * @return Vector containing organism hierarchy
     */

    private Vector getOrganismTree(TaxonNode taxonNode){
        List<TaxonNode> childrenList = taxonNode.getChildren();
        Vector childrenVector = new Vector();
        for(TaxonNode child : childrenList){

           if(child.isLeaf()){
               //if(child.isPopulated()){
                   childrenVector.add(child.getLabel());
               //}
           }else{
               childrenVector.add(getOrganismTree(child));
           }
        }
        return new NamedVector(taxonNode.getLabel(), childrenVector);
    }

    /**
     * The name of the plug-in, maybe this should be set in the config
     *
     * @return the name
     */
    public String getName(){
        return "Organism Tree";
    }

    /**
     * Is there only one instance of the plug-in, by default
     *
     * @return true if there should only be one copy of the plug-in
     */
    public boolean isSingletonByDefault(){
        return false; //Shouldn't this be true by default?
    }

    /**
     * Allow the plug-in to indicate whether it has unsaved changes
     *
     * @return true if there are changes to be saved
     */
    public boolean isUnsaved(){
        return false;
    }

    /**
     * @param newArgs
     */
    public void process(List<String> newArgs){
        //What is this meant to do?
    }

    @Override
    public void setJogra(Jogra jogra) {
        // TODO Auto-generated method stub
        
    }

}
