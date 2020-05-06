package firebase.app.testcrud.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import firebase.app.testcrud.R;
import firebase.app.testcrud.viewmodel.DirectorioViewModel;

public class DirectorioFragment extends Fragment {

    private DirectorioViewModel mViewModel;

    public static DirectorioFragment newInstance() {
        return new DirectorioFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.directorio_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DirectorioViewModel.class);
        // TODO: Use the ViewModel
    }

}
