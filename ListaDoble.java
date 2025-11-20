package eightoff;

public class ListaDoble<T>
{
    private NodoDoble<T> inicio;

    public ListaDoble() 
    {
        inicio = null;
    }

    public boolean isEmpty() 
    {
        return inicio == null;
    }

    public NodoDoble<T> insertarInicio(T datoNuevo) 
    {
        NodoDoble<T> n = new NodoDoble<>(datoNuevo);
        n.setSig(inicio);
        if (inicio == null)
            inicio = n;
        else 
        {
            inicio.setAnt(n);
            inicio = n;
        }
        return n;
    }

    public NodoDoble<T> insertarDespuesDeUnNodo(NodoDoble<T> nodoAnterior, T datoNuevo) 
    {
        if (nodoAnterior == null)
            return insertarInicio(datoNuevo);

        NodoDoble<T> n = new NodoDoble<>(datoNuevo);
        n.setAnt(nodoAnterior);
        n.setSig(nodoAnterior.getSig());

        if (nodoAnterior.getSig() != null)
            nodoAnterior.getSig().setAnt(n);
        nodoAnterior.setSig(n);
        return n;
    }

    public void cortarDesdeUnNodo(NodoDoble<T> nodo) {
        if (nodo == null)
            inicio = null;
        else
            nodo.setSig(null);
    }
    
    public T getInfo(NodoDoble<T> n) 
    {
        if(n == null)
            return null;
        else
            return n.getInfo();
    }

    public T eliminarInicio() 
    {
        T datoEliminado = null;
        if (inicio == null)
            System.out.println("Lista vacía.");
        else 
        {
            datoEliminado = inicio.getInfo();
            if (inicio.getSig() == null)
                inicio = null;
            else 
            {
                inicio = inicio.getSig();
                inicio.setAnt(null);
            }
        }
        return datoEliminado;
    }

    public T eliminarFin() 
    {
        T datoEliminado = null;
        if (inicio == null)
            System.out.println("Lista vacía.");
        else 
            if (inicio.getSig() == null) 
            {
                datoEliminado = inicio.getInfo();
                inicio = null;
            } 
            else 
            {
                NodoDoble<T> r = inicio;
                while (r.getSig() != null)
                    r = r.getSig();
                datoEliminado = r.getInfo();
                r.getAnt().setSig(null);
            }
        return datoEliminado;
    }
    
    public void ordenarLista() 
    {
        if (inicio == null || inicio.getSig() == null) 
            return;
        NodoDoble<T> r = inicio, n; T aux;
        while (r.getSig() != null) 
        {
            n = r.getSig();
            while (n != null) 
            {
                if (((Comparable<T>) r.getInfo()).compareTo(n.getInfo()) > 0) {
                    aux = r.getInfo();
                    r.setInfo(n.getInfo());
                    n.setInfo(aux);
                }
                n = n.getSig();
            }
            r = r.getSig();
        }
    }
    
    public void eliminarPares() {
         if (inicio == null) 
         {
            System.out.println("Lista vacía."); 
            return;
         }
        NodoDoble<T> r = inicio;
        while (r != null) 
        {
            NodoDoble<T> siguiente = r.getSig();
            if ((Integer) r.getInfo() % 2 == 0)
                if (r == inicio)
                    eliminarInicio();
                else 
                {
                    r.getAnt().setSig(r.getSig());
                    if (r.getSig() != null) 
                        r.getSig().setAnt(r.getAnt());
                }
            r = siguiente;
        }
    }

    public NodoDoble<T> getInicio() {
        return inicio;
    }
}
