package eightoff;

public class ListaSimple<T>
{
    private Nodo inicio;
    
    public ListaSimple()
    {
        inicio = null;
    }
    
    public void insertarInicio(T datoNuevo)
    {
        Nodo n = new Nodo();
        n.setDato(datoNuevo);
        n.setSig(inicio);
        inicio = n;
    }
    
    public void insertarFin(T datoNuevo)
    {
        Nodo n = new Nodo();
        n.setDato(datoNuevo);
        if(inicio == null)
        {
            n.setSig(inicio);
            inicio = n;
        }
        else
        {
                Nodo r = new Nodo();
                r = inicio;
                while(r.getSig() != null)
                    r = r.getSig();
                r.setSig(n);
        }
    }
    
    public T eliminarInicio()
    {
        T datoEliminado = null;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
        {
            datoEliminado = (T) inicio.getDato();
            inicio = inicio.getSig();
        }
        return datoEliminado;
    }
    
    public T eliminarFin()
    {
        T datoEliminado = null;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
            if(inicio.getSig() == null)
            {
                datoEliminado = (T) inicio.getDato();
                inicio = null;
            }
            else
            {
                Nodo r = inicio;
                Nodo a = r;
                while(r.getSig() != null)
                {
                    a = r;
                    r = r.getSig();
                }
                datoEliminado = (T) r.getDato();
                a.setSig(null);
            }
        return datoEliminado;
    }
    
    public String mostrarRecursivo(Nodo indice)
    {
        if(inicio == null)
            return "Lista vacía.";
        else
            if(indice.getSig() == null)
                return "" + indice.getDato() + ".";
            else
                return "" + indice.getDato() + ", " + mostrarRecursivo(indice.getSig());
    }
    
    public T eliminaX(T datoAEliminar)
    {
        T datoEliminado = null;
        Nodo r;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
            if(inicio.getDato().equals(datoAEliminar))
            {
                datoEliminado = (T) inicio.getDato();
                inicio = inicio.getSig();
            }
            else
            {
                r = inicio;
                while(r.getSig() != null)
                    if(r.getSig().getDato().equals(datoAEliminar))
                    {
                        datoEliminado = (T) r.getSig().getDato();
                        r.setSig(r.getSig().getSig());
                    }
                    else
                        r = r.getSig();
                
            }
        return datoEliminado;
    }
    
    public int buscarX(T datoABuscar, Nodo indice, int posicion)
    {
        if(inicio == null)
        {
            System.out.println("Lista vacía.");
            return -1;
        }
        else
            if(indice.getDato().equals(datoABuscar))
                return posicion;
            else
                if(indice.getSig() == null)
                {
                    System.out.println("Dato no encontrado.");
                    return -1;
                }
                else
                    return buscarX(datoABuscar, indice.getSig(), posicion+1);
    }
    
    public T eliminaPosicion(int posicion)
    {
        T datoEliminado = null;
        int numeroNodoActual = 0;
        if(inicio == null)
            System.out.println("Lista vacía.");
        else
        {
            Nodo r = inicio;
            if(posicion == 0)
            {
                datoEliminado = (T) inicio.getDato();
                inicio = inicio.getSig();
            }
            else
            {
                while(r.getSig() != null)
                {
                    if(numeroNodoActual+1 == posicion)
                    {
                        datoEliminado = (T) r.getSig().getDato();
                        r.setSig(r.getSig().getSig());
                        posicion = 0;
                    }
                    else
                    {
                        numeroNodoActual++;
                        r = r.getSig();
                    }
                }
                if(posicion > numeroNodoActual || posicion < 0)
                    System.out.println("Posición inexistente.");
            }
        }
        return datoEliminado;
    }
    
    public T buscaPosicion(int posicion)
    {
        if (isEmpty() || posicion < 0)
            return null;

        Nodo r = inicio;
        int contador = 0;
        while (r != null)
        {
            if (contador == posicion)
                return (T) r.getDato();

            r = r.getSig();
            contador++;
        }
        return null; 
    }
    
   public void ordenarLista()
    {
        if(inicio == null) 
        {
            System.out.println("Lista vacía.");
            return;
        }

        if(inicio.getSig() == null) 
        {
            System.out.println("La lista solo tiene un dato.");
            return;
        }

        Nodo r;
        Nodo a = inicio;
        T auxiliar = null;
        while(a.getSig() != null)
        {
            r = a.getSig();
            while(r != null)
            {
                Comparable datoA = (Comparable) a.getDato();
                Comparable datoR = (Comparable) r.getDato();

                if(datoA.compareTo(datoR) > 0)
                {
                    auxiliar = (T) a.getDato();
                    a.setDato(r.getDato());

                    r.setDato(auxiliar);
                }
                r = r.getSig();
            }
            a = a.getSig();
        }
    }
    
    public void insertarEnPosicion(T dato, int posicion)
    {
        int numeroNodoActual = 0;
        Nodo n = new Nodo();
        n.setDato(dato);
        if(posicion == 0)
        {
            n.setSig(inicio);
            inicio = n;
        }
        else
        {
            Nodo r = inicio;
            while(r.getSig() != null || posicion == numeroNodoActual+1)
            {
                if(posicion == numeroNodoActual+1)
                {
                    n.setSig(r.getSig());
                    r.setSig(n);
                    posicion = 0;
                }
                else
                    if(r.getSig() != null)
                    {
                        r = r.getSig();
                        numeroNodoActual++;
                    }
            }
        }
        if(posicion > numeroNodoActual || posicion < 0)
            System.out.println("Posición inalcanzable.");
    }
    
    public void agregarTodo(ListaSimple<T> otraLista)
    {
        if (otraLista == null || otraLista.isEmpty())
            return;

        Nodo r = otraLista.getInicio();
        while (r != null)
        {
            this.insertarFin((T) r.getDato()); 
            r = r.getSig();
        }
    }
    
    public boolean isEmpty()
    {
        return inicio == null;
    }
    
    public int getTamano()
    {
        int tamano = 0;
        Nodo r = inicio;
        while (r != null)
        {
            tamano++;
            r = r.getSig();
        }
        return tamano;
    }
    
    public Nodo getInicio()
    {
        return inicio;
    }
    
    public T getPrimerDato()
    {
        if (isEmpty())
            return null;
        return (T) inicio.getDato();
    }
    
    public T getUltimoDato()
    {
        if (isEmpty())
            return null;
        else
        {
            Nodo r = inicio;
            while (r.getSig() != null)
                r = r.getSig();
            return (T) r.getDato();
        }
    }
}
