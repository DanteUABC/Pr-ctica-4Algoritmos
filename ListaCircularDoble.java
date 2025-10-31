package eightoff;

import java.util.Random;

public class ListaCircularDoble<T extends Comparable<T>>
{
    private NodoDoble inicio, fin;
    
    public ListaCircularDoble()
    {
        inicio = null;
        fin = null;
    }
    
    public void insertarInicio(T dato)
    {
        if(inicio == null)
        { 
            fin = inicio = new NodoDoble(dato);
            inicio.setSig(inicio);
            inicio.setAnt(inicio);
        }
        else
        {
            NodoDoble n = new NodoDoble(dato);
            n.setSig(inicio);
            inicio.setAnt(n);
            n.setAnt(fin);
            inicio = n;
            fin.setSig(inicio);
        }
    }
    
    public void insertarFin(T dato)
    {
        if(inicio == null)
        {
            fin = inicio = new NodoDoble(dato);
            inicio.setSig(inicio);
            inicio.setAnt(inicio);
        }
        else
        {
            NodoDoble n = new NodoDoble(dato);
            n.setSig(inicio);
            inicio.setAnt(n);
            fin.setSig(n);
            n.setAnt(fin);
            fin = n;
        }
    }
    
    public T eliminarInicio()
    {
        T dato = null;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
        {
            dato = (T) inicio.getInfo();
            if(inicio == fin)
                inicio = fin = null;
            else
            {
                fin.setSig(inicio.getSig());
                inicio = inicio.getSig();
                inicio.setAnt(fin);
            }
        }
        return dato;
    }
    
    public T eliminarFin()
    {
        T dato = null;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
        {
            dato = (T) fin.getInfo();
            if(inicio == fin)
                inicio = fin = null;
            else
            {
                inicio.setAnt(fin.getAnt());
                fin.getAnt().setSig(inicio);
                fin = fin.getAnt();
            }
        }
        return dato;
    }
    
    public int getTamano()
    {
        int tamano = 0;
        if(inicio == null)
            return 0;
        else
            if(inicio == fin)
                return 1;
            else
            {
                NodoDoble r = inicio;
                tamano++;
                while(r.getSig() != inicio)
                {
                    r = r.getSig();
                    tamano++;
                }
                return tamano;
            }
    }
    
    public void ordenar()
    {
        if (inicio == null || inicio == fin)
            return;

        int n = getTamano();
        boolean swapped;

        for (int i = 0; i < n - 1; i++) 
        {
            NodoDoble r = inicio;
            swapped = false;
            
            for (int j = 0; j < n - 1 - i; j++) 
            {
                T data1 = (T) r.getInfo();
                T data2 = (T) r.getSig().getInfo();

                if (data1.compareTo(data2) > 0) 
                {
                    r.setInfo(data2);
                    r.getSig().setInfo(data1);
                    swapped = true;
                }
                r = r.getSig();
            }

            if (!swapped)
                break;
        }
    }
    
    public void barajear()
    {
        if (inicio == null || inicio == fin)
            return;

        Random rand = new Random();
        int n = getTamano();
        NodoDoble r = fin;

        for (int i = n; i > 1; i--) 
        {
            int j = rand.nextInt(i); 

            NodoDoble s = inicio;
            for (int k = 0; k < j; k++) 
            {
                s = s.getSig();
            }

            if (r != s) 
            {
                T temp = (T) r.getInfo();
                r.setInfo(s.getInfo());
                s.setInfo(temp);
            }

            r = r.getAnt();
        }
    }
}
