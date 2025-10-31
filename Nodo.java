package eightoff;

public class Nodo<T>
{
    private Nodo sig;
    private T dato;
    
    public Nodo()
    {
        sig = null;
        dato = null;
    }
    
    public Nodo(T nuevoDato, Nodo nuevoSig)
    {
        sig = nuevoSig;
        dato = nuevoDato;
    }
    
    public T getDato()
    {
        return dato;
    }
    
    public Nodo getSig()
    {
        return sig;
    }
    
    public void setDato(T nuevoDato)
    {
        dato = nuevoDato;
    }
    
    public void setSig(Nodo nuevoSig)
    {
        sig = nuevoSig;
    }
}
