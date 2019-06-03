package model;

/**
 * Not a real model (no db table) of a Headmaster user
 * <p>
 * A Headmaster has a {@link model.User#username username}, 
 * a {@link model.User#password password}, 
 * a {@link model.User#saltKey salt key}, 
 * a {@link model.User#id unique id} and 
 * a {@link model.User#type type}, 
 * inherited from {@link model.User User}
 *
 * @author Sakel
 */
public final class Headmaster extends User {

    public Headmaster(){
        super();
    }

    private Headmaster(String type, String username, byte[] password, byte[] saltKey) {
        super(type, username, password, saltKey);
    }

    private Headmaster(String type, String username, byte[] password, byte[] saltKey, int id) {
        super(type, username, password, saltKey, id);
    }

    @Override
    public Headmaster factory(String type, String username, byte[] password, byte[] saltKey, Object... args){

        Headmaster h = new Headmaster(type, username, password, saltKey);

        return h;
    }

    @Override
    public Headmaster factory(String type, String username, byte[] password, byte[] saltKey, int id, Object... args){

        Headmaster h = new Headmaster(type, username, password, saltKey, id);

        return h;
    }

}
