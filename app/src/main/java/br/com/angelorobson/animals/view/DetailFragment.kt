package br.com.angelorobson.animals.view

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import br.com.angelorobson.animals.R
import br.com.angelorobson.animals.model.Animal
import br.com.angelorobson.animals.util.getProgressDrawable
import br.com.angelorobson.animals.util.loadImage
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.item_animal.animalImage
import kotlinx.android.synthetic.main.item_animal.animalName


class DetailFragment : Fragment() {

    var animal: Animal? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            animal = DetailFragmentArgs.fromBundle(it).animal
        }

        animalImage.loadImage(animal?.imageUrl, getProgressDrawable(requireContext()))
        animalName.text = animal?.name
        animalLocation.text = animal?.location
        animalLifespan.text = animal?.lifeSpan
        animalDiet.text = animal?.diet

        animal?.imageUrl?.let {
            setupBackgroundColor(it)
        }
    }

    private fun setupBackgroundColor(url: String?) {
        Glide.with(this)
            .asBitmap()
            .load(url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onLoadCleared(placeholder: Drawable?) {}

                override fun onResourceReady(
                    resource: Bitmap,
                    transition: Transition<in Bitmap>?
                ) {
                    Palette.from(resource)
                        .generate { pallet ->
                            val intColor = pallet?.lightMutedSwatch?.rgb ?: 0
                            animalLayout.setBackgroundColor(intColor)
                        }
                }

            })
    }

}
