package br.com.angelorobson.animals.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import br.com.angelorobson.animals.R
import br.com.angelorobson.animals.model.Animal
import br.com.angelorobson.animals.util.getProgressDrawable
import br.com.angelorobson.animals.util.loadImage
import kotlinx.android.synthetic.main.item_animal.view.*

class AnimalListAdapter(private val animalList: ArrayList<Animal>) :
    RecyclerView.Adapter<AnimalListAdapter.AnimalViewHolder>() {

    fun updateAnimalList(newAnimalList: List<Animal>) {
        animalList.clear()
        animalList.addAll(newAnimalList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_animal, parent, false)
        return AnimalViewHolder(view)
    }

    override fun getItemCount() = animalList.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        val animal = animalList[position]
        holder.view.animalName.text = animal.name
        holder.view.animalImage.loadImage(
            animal.imageUrl,
            getProgressDrawable(holder.view.context)
        )
        holder.view.animalLayout.setOnClickListener {
            val action = ListFragmentDirections.actionDetail(animal)
            Navigation.findNavController(holder.view).navigate(action)
        }
    }

    class AnimalViewHolder(val view: View) : RecyclerView.ViewHolder(view)

}