package com.pmd.rentavehiculos.ui.theme


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pmd.rentavehiculos.model.Vehiculo


class VehiculoAdapter(private val vehiculos: List<Vehiculo>) :
    RecyclerView.Adapter<VehiculoAdapter.VehiculoViewHolder>() {

    class VehiculoViewHolder(private val binding: ItemVehiculoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(vehiculo: Vehiculo) {
            binding.textMarca.text = vehiculo.marca
            binding.textModelo.text = vehiculo.modelo
            binding.textPrecio.text = "Precio: ${vehiculo.precioPorDia} €/día"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VehiculoViewHolder {
        val binding = ItemVehiculoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VehiculoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VehiculoViewHolder, position: Int) {
        holder.bind(vehiculos[position])
    }

    override fun getItemCount(): Int = vehiculos.size
}
