/*
 * RomRaider Open-Source Tuning, Logging and Reflashing
 * Copyright (C) 2006-2022 RomRaider.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.romraider.swing;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeCellRenderer;

import com.romraider.maps.Rom;
import com.romraider.maps.RomID;
import com.romraider.maps.Table;

public class RomCellRenderer implements TreeCellRenderer {

    JLabel fileName;
    JLabel carInfo;
    DefaultTreeCellRenderer defaultRenderer = new DefaultTreeCellRenderer();

    static ImageIcon icon1D = new ImageIcon(RomCellRenderer.class.getResource("/graphics/1d.gif"));
    static ImageIcon icon2D = new ImageIcon(RomCellRenderer.class.getResource("/graphics/2d.gif"));
    static ImageIcon icon3D = new ImageIcon(RomCellRenderer.class.getResource("/graphics/3d.gif"));
    static ImageIcon iconSwitch = new ImageIcon(RomCellRenderer.class.getResource("/graphics/switch.gif"));

    public RomCellRenderer() {
        fileName = new JLabel(" ");
        fileName.setFont(new Font("Tahoma", Font.BOLD, 11));
        fileName.setHorizontalAlignment(JLabel.CENTER);

        carInfo = new JLabel(" ");
        carInfo.setFont(new Font("Tahoma", Font.PLAIN, 10));
        carInfo.setHorizontalAlignment(JLabel.CENTER);
    }
    
    public static ImageIcon getIconForTable(Table t) {   	
        // display icon
        if (t.getType() == Table.TableType.TABLE_1D) {
        	return icon1D;
        } else if (t.getType() == Table.TableType.TABLE_2D) {
        	return icon2D;     
        } else if (t.getType() == Table.TableType.TABLE_3D) {
        	return icon3D;
        } else if (t.getType() == Table.TableType.SWITCH) {
        	return iconSwitch;
        }
        
        return null;
    }
    
    private String buildCarInfoText(Rom rom) {
    	String carInfoText = "<html>";
        RomID id = rom.getRomID();
        
        if(id.getVersion() != null)
        	carInfoText+= "<B><font color=blue>" + id.getVersion() + " </font></B>";
        	
        if(rom.getRomIDString() != null)
        	carInfoText+=rom.getRomIDString() + ", ";
        
        if(id.getCaseId() != null)
        	carInfoText+=id.getCaseId() + "; ";
        
        if(id.getYear() != null)
        	carInfoText+=id.getYear() + " ";
        
        if(id.getMake() != null)
        	carInfoText+=id.getMake() + " ";
        
        if(id.getModel() != null)
        	carInfoText+=id.getModel() + " "; 
        
        if(id.getSubModel() != null)
        	carInfoText+=id.getSubModel(); 
        
        if(id.getTransmission() != null)
        	carInfoText+=", " + id.getTransmission();
        
        if(carInfoText.endsWith(", ") || carInfoText.endsWith("; ")) 
        	carInfoText = carInfoText.substring(0, carInfoText.length() - 2);          
        
        if(id.getAuthor() != null)
        	carInfoText+=" by " + id.getAuthor();
        
        carInfoText+= "</html>";
        
        return carInfoText;
    }
    
    @Override
    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {

        Component returnValue = null;

        if (value != null && value instanceof Rom) {
            Rom rom = ((Rom) value);

            if (expanded) {
                fileName.setText("- " + rom.getFileName());
            } else {
                fileName.setText("+ " + rom.getFileName());
            }
                   
            carInfo.setText(buildCarInfoText(rom));
            
            JPanel renderer = new JPanel(new GridLayout(2, 1));
            renderer.add(fileName);
            renderer.add(carInfo);

            // Use the tree's own selection/text colors (which the active
            // L&F derives from the OS theme) instead of hardcoded pastel
            // colors, so this stays readable under both light and dark
            // themes instead of only the light theme these were tuned for.
            if (selected) {
                renderer.setOpaque(true);
                Color selectionBg = UIManager.getColor("Tree.selectionBackground");
                Color selectionFg = UIManager.getColor("Tree.selectionForeground");
                renderer.setBackground(selectionBg);
                fileName.setForeground(selectionFg);
                carInfo.setForeground(selectionFg);
            } else {
                renderer.setOpaque(false);
                Color textFg = UIManager.getColor("Tree.textForeground");
                fileName.setForeground(textFg);
                carInfo.setForeground(textFg);
            }

            renderer.setPreferredSize(new Dimension(tree.getParent().getWidth(), 30));
            renderer.setMaximumSize(new Dimension(tree.getParent().getWidth(), 30));
            renderer.setEnabled(tree.isEnabled());
            returnValue = renderer;
        } else if (value != null && value instanceof TableTreeNode) {

            Table table = (Table) (((TableTreeNode)(value)).getUserObject());
            JPanel renderer = new JPanel(new GridLayout(1, 1));
            JLabel tableName = new JLabel (table.getName() + " ", getIconForTable(table), JLabel.LEFT);
            renderer.add(tableName);
            tableName.setFont(new Font("Tahoma", Font.PLAIN, 11));

            // Use the tree's own selection/text colors (which the active
            // L&F derives from the OS theme) instead of hardcoded white/
            // black/pastel colors, so this stays readable under both light
            // and dark themes instead of only the light theme these were
            // tuned for.
            if (selected) {
                renderer.setOpaque(true);
                renderer.setBackground(UIManager.getColor("Tree.selectionBackground"));
                tableName.setForeground(UIManager.getColor("Tree.selectionForeground"));
            } else {
                renderer.setOpaque(false);
                tableName.setForeground(UIManager.getColor("Tree.textForeground"));
            }

            if (table.getUserLevel() == 5) {
                // A fixed mid-tone red rather than a light pink: this label
                // no longer sits on a fixed white background, so it now
                // needs to read against either a light or dark tree
                // background depending on the OS theme.
                tableName.setForeground(new Color(200, 60, 60));
                tableName.setFont(new Font("Tahoma", Font.ITALIC, 11));

            } else if (table.getUserLevel() > table.getSettings().getUserLevel()) {
                tableName.setFont(new Font("Tahoma", Font.ITALIC, 11));

            }

            returnValue = renderer;
        }

        if (returnValue == null) {
            returnValue = defaultRenderer.getTreeCellRendererComponent(tree,
                    value, selected, expanded, leaf, row, hasFocus);
        }

        return returnValue;

    }
}
