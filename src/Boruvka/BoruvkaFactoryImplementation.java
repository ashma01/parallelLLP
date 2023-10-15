package Boruvka;

public class BoruvkaFactoryImplementation implements BoruvkaFactory {
    @Override
    public Boruvka createBoruvkaInstance(int[] mwe) {
        return new Boruvka(mwe);
    }
}