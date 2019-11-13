package br.com.livroandroid.trainingmockup.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import br.com.livroandroid.trainingmockup.Entities.Card;
import br.com.livroandroid.trainingmockup.R;

public class Adapter extends PagerAdapter {

    private List<Card> cards;
    private LayoutInflater layoutInflater;
    private Context context;

    public Adapter(List<Card> cardAdapter, Context context) {
        this.cards = cardAdapter;
        this.context = context;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item,container,false);

        ImageView imageView;
        TextView temperature;

        imageView = view.findViewById(R.id.imageViewItem);
        temperature = view.findViewById(R.id.tvCardViewTitle);

        //imageView.setImageResource(cards.get(position).getImageUrl());
        temperature.setText(" " + cards.get(position).getTemp() + " °C");
        Picasso.with(context)
                .load(cards.get(position).getImageUrl())
                .fit()
                .centerCrop()
                .into(imageView);

        //AKi era pra gerar a imagem dentro do CardView....mas credito q á URL ta errada.

        container.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }

}
