/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.weinhaendler;

import java.util.LinkedHashSet;

/**
 *
 * @author sfrie
 */
public class ReplacingSet<E> extends LinkedHashSet<E> {
    @Override
    public boolean add(E e){
        super.remove(e);
        return super.add(e);
    }
}
