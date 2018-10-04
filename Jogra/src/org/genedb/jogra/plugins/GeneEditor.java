/*
 * Copyright (c) 2007 Genome Research Limited.
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

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

import org.slf4j.Logger;import org.slf4j.LoggerFactory;
import org.bushe.swing.event.EventBus;
import org.genedb.jogra.domain.Gene;
import org.genedb.jogra.domain.GeneDBMessage;
import org.genedb.jogra.drawing.Jogra;
import org.genedb.jogra.drawing.JograPlugin;
import org.genedb.jogra.drawing.OpenWindowEvent;
import org.genedb.jogra.services.GeneService;
import org.genedb.jogra.services.LockAndNotificationService;
import org.genedb.jogra.services.LockStatus;

public class GeneEditor implements JograPlugin {

	private static final Logger logger = LoggerFactory.getLogger(GeneEditor.class);
	
	private LockAndNotificationService lockAndNotificationService;
	private GeneService geneService;

    public JFrame getMainPanel(final Gene gene) {
        JFrame ret = new JFrame();
        GeneViewModel model = new GeneViewModel();
        model.setGene(gene);
        GeneView gv = new GeneView(model);
        ret.add(gv);
        ret.pack();
        //ret.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ret.setVisible(true);
        return ret;
    }
	
//    public JFrame getMainPanel(final Gene gene) {
//
//    	boolean conflict = false;
//    	LockStatus lockStatus = lockAndNotificationService.lockGene(gene.getUniqueName());
//    	if (lockStatus == null) {
//    		conflict = true;
//    	}
//
//    	
//		//Session session = hibernateTransactionManager.getSessionFactory().openSession();
//		//Transaction transaction = session.beginTransaction();
//    	
//        final GeneEditorFrame ret = new GeneEditorFrame();
//        ret.setLockStatus(lockStatus);
//        ret.setGene(gene);
//        
//        ret.setTitle(gene.getUniqueName());
//        ret.setLayout(new BorderLayout());
//        
//        final FormLayout fl = new FormLayout("pref 4dlu pref 2dlu pref",
//                "pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref 2dlu pref");
//        JPanel main = new JPanel(fl);
//        // JPanel ret = new JPanel(fl);
//        // ret.add(new JLabel("Hello"));
//        // ret.add(new JLabel("World"));
//        final CellConstraints cc = new CellConstraints();
//
//        Box topBar = Box.createHorizontalBox();
//        topBar.add(new JLabel(ConflictComponentFactory.getConflictString(conflict)), cc.xy(3, 1));
//        topBar.add(new JLabel(getConflictIcon(conflict)), cc.xy(1, 1));
//        topBar.add(Box.createHorizontalGlue());
//        ret.add(topBar, BorderLayout.NORTH);
//        
//        final JTextField instance = new JTextField(gene.getUniqueName());
//        main.add(new JLabel("Systematic id"), cc.xy(1, 3));
//        main.add(instance, cc.xy(3, 3));
//
//        String org = gene.getOrganism();
//        final JLabel organism = new JLabel(org);
//        main.add(new JLabel("Organism"), cc.xy(1, 5));
//        main.add(organism, cc.xy(3, 5));
//
//        final JTextField username = new JTextField(gene.getName());
//        main.add(new JLabel("Name"), cc.xy(1, 7));
//        main.add(username, cc.xy(3, 7));
//
//        final JTextField synonyms = new JTextField(StringUtils.collectionToCommaDelimitedString(gene.getSynonyms()));
//        main.add(new JLabel("Synonyms"), cc.xy(1, 9));
//        main.add(synonyms, cc.xy(3, 9));
//
//        int ypos = 11;
//        final JTextField reservedName = new JTextField(gene.getReservedName());
//        main.add(new JLabel("Reserved Name"), cc.xy(1, ypos));
//        main.add(reservedName, cc.xy(3, ypos));
//        
//        ypos += 2;
//        final JTextField product = new JTextField(StringUtils.collectionToCommaDelimitedString(gene.getProducts()));
//        main.add(new JLabel("Product"), cc.xy(1, ypos));
//        main.add(product, cc.xy(3, ypos));
//
//        ypos += 2;
//        //final JTextField orthologues = new JTextField();
//        String text = ""+gene.getOrthologues().size()+" orthologues";
//        if (gene.getOrthologues().size()< 3 ) {
//        	text = StringUtils.collectionToCommaDelimitedString(gene.getOrthologues());
//        }
//        final JTextField orthologues = new JTextField(text);
//        main.add(new JLabel("Orthologues"), cc.xy(1, ypos));
//        main.add(orthologues, cc.xy(3, ypos));
//        main.add(new JButton("List"), cc.xy(5, ypos));
//
//        ypos += 2;
//        text = ""+gene.getParalogues().size()+" paralogues";
//        if (gene.getParalogues().size() < 3 ) {
//        	text = StringUtils.collectionToCommaDelimitedString(gene.getParalogues());
//        }
////        final JTextField paralogues = new JTextField();
//        final JTextField paralogues = new JTextField(text);
//        main.add(new JLabel("Paralogues"), cc.xy(1, ypos));
//        main.add(paralogues, cc.xy(3, ypos));
//        main.add(new JButton("List"), cc.xy(5, ypos));
//
//        ypos += 2;
//        text = ""+gene.getClusters().size()+" clusters";
//        if (gene.getClusters().size()< 3 ) {
//        	text = StringUtils.collectionToCommaDelimitedString(gene.getClusters());
//        }
//        final JTextField clusters = new JTextField(text);
//        main.add(new JLabel("Clusters"), cc.xy(1, ypos));
//        main.add(clusters, cc.xy(3, ypos));
//        main.add(new JButton("List"), cc.xy(5, ypos));
//        
//        ret.add(new JScrollPane(main), BorderLayout.CENTER);
//        
//        ret.add(new JButton("Close"), BorderLayout.SOUTH);
//        ret.pack();
//        
//        ret.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
//        ret.addWindowListener(new WindowAdapter() {
//
//			public void windowClosing(WindowEvent e) {
//				System.err.println("Window closed");
//				GeneEditorFrame source = (GeneEditorFrame) e.getWindow();
//				if (source.getLockStatus() != null) {
//					System.err.println("Trying to unlock gene");
//					lockAndNotificationService.unlockGene(source.getGene().getUniqueName());
//					source.setGene(null);
//					source.setLockStatus(null);
//				}
//				//source.dispose();
//			}
//
////			public void windowClosing(WindowEvent e) {
////				// TODO Auto-generated method stub
////				System.err.println("Window about to close");
////			}
//        	
//        });
//        
//        //transaction.commit();
//        return ret;
//    }

    private Icon getConflictIcon(boolean conflict) {
		return ConflictComponentFactory.getConflictIcon(conflict);
	}

	public JPanel getMainWindowPlugin() {
        logger.error("Looking for plugin for the main window");
        final JPanel ret = new JPanel();
        final Box box = Box.createVerticalBox();
        final JLabel label = new JLabel("Gene Editor Search");
        box.add(label);

        final Box row = Box.createHorizontalBox();

        final JTextField query = new JTextField("PFA0760w");
        row.add(query);
        final JButton search = new JButton("Search");
        row.add(search);
        search.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent ae) {
                System.err.println("Am I on EDT '" + EventQueue.isDispatchThread() + "' 1");
                new SwingWorker<JFrame, Void>() {

                    @Override
                    protected JFrame doInBackground() throws Exception {
                        return makeWindow(query.getText());
                    }

                    @Override
                    protected void done() {
                        try {
                            final JFrame result = get();
                            final GeneDBMessage e = new OpenWindowEvent(GeneEditor.this, result);
                            EventBus.publish(e);
                        } catch (final InterruptedException exp) {
                            exp.printStackTrace();
                        } catch (final ExecutionException exp) {
                            exp.printStackTrace();
                        }

                    }
                }.execute();
            }
        });
        box.add(row);
        ret.add(box);
        logger.error("Returning a plugin for the main window");
        return ret;
    }

    public String getName() {
        return "Gene Editor";
    }

    public int getOrder() {
        return 1;
    }

    public boolean isSingletonByDefault() {
        return false;
    }

    public boolean isUnsaved() {
        // TODO Auto-generated method stub
        return false;
    }

    private JFrame makeWindow(final String search) {
        System.err.println("Am I on EDT '" + EventQueue.isDispatchThread() + "'  x");
        final String title = "Gene: " + search;
        //Feature f = sequenceDao.getFeatureByUniqueName(search, "gene");
        //Gene gene = new Gene(f);
        List<String> geneNames = geneService.findTranscriptNamesByPartialName(search);
        System.err.println("Size="+geneNames.size());
        for (String string : geneNames) {
			System.err.println(string);
		}
        if (geneNames.size()==0) {
        	//return displayGenes(search, title);
        }
        if (geneNames.size()==1) {
        	return displayGenes(geneNames.get(0), title);
        }
        return displayGeneNameList(geneNames, title);
    }

	private JFrame displayGenes(final String search, final String title) {
		Gene gene = geneService.findGeneByUniqueName(search);
		if (gene==null) {
			System.err.println("Can't find gene called '"+search+"'");
			return null;
		}
		JFrame lookup = Jogra.findNamedWindow(title);
		if (lookup == null) {
			lookup = getMainPanel(gene);
		}
		return lookup;
	}
	
	private JFrame displayGeneNameList(final List<String> geneNames, final String title) {
		GeneList gl = new GeneList(); // FIXME
		return gl.getMainPanel(title, geneNames);
	}

	public void setGeneService(GeneService geneService) {
		this.geneService = geneService;
	}

	public void setLockAndNotificationService(LockAndNotificationService lockAndNotificationService) {
		this.lockAndNotificationService = lockAndNotificationService;
	}

	public void process(List<String> newArgs) {
		final String query = newArgs.get(0);
        new SwingWorker<JFrame, Void>() {

            @Override
            protected JFrame doInBackground() throws Exception {
                return makeWindow(query);
            }

            @Override
            protected void done() {
                try {
                    final JFrame result = get();
                    final GeneDBMessage e = new OpenWindowEvent(GeneEditor.this, result);
                    EventBus.publish(e);
                } catch (final InterruptedException exp) {
                    exp.printStackTrace();
                } catch (final ExecutionException exp) {
                    exp.printStackTrace();
                }

            }
        }.execute();
	}

    @Override
    public void setJogra(Jogra jogra) {
        // TODO Auto-generated method stub
        
    }
	
}

class GeneEditorFrame extends JFrame {
	private LockStatus lockStatus;
	private Gene gene;

	public LockStatus getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(LockStatus lockStatus) {
		this.lockStatus = lockStatus;
	}

	public Gene getGene() {
		return gene;
	}

	public void setGene(Gene gene) {
		this.gene = gene;
	}
	
	
	
}


