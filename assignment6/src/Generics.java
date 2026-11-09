interface IComparator<T> {
   boolean apply(T t);
}

interface IList<T> {
    IList<T> sort(IComparator<T> comp);
}


class MtList<T> implements IList<T> {
    MtList() {};

    public IList<T> sort(IComparator<T> comp) {
        return new MtList<T>();
    }
}

class ConsList<T> implements IList<T> {
    T first;
    IList<T> rest;

    ConsList(T first, IList<T> rest) {
        this.first = first;
        this.rest = rest;
    }
    
    public IList<T> sort(IComparator<T> comp) {
    if (comp.apply(this.first)) {
        return new ConsList<T>(this.first, this.rest.sort(comp));
    }
    else {
        return this.rest.sort(comp);
    }
    }
}

