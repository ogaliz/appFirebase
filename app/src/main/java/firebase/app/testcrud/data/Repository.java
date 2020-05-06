package firebase.app.testcrud.data;

import com.google.firebase.database.core.Context;

public class Repository {

//  Para poder escribir en la base de datos necesitamos el contexto
    private static Repository repository;
    private Context context;

    private Repository(Context context) {

        this.context = context;
    }

    public static Repository get(Context context){

        if (repository == null){
            Repository.repository = new Repository(context);
        }
        return repository;
    }

    public static Repository getRepository(){
        return repository;
    }
}
