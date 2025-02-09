package com.pmd.rentavehiculos.screen


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pmd.rentavehiculos.ui.theme.VehiculoAdapter
import com.pmd.rentavehiculos.viewmodel.VehiculoViewModel


class VehiculoFragment : Fragment() {
    private var _binding: FragmentVehiculosBinding? = null
    private val binding get() = _binding!!
    private val viewModel: VehiculoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentVehiculosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerViewVehiculos.layoutManager = LinearLayoutManager(requireContext())

        viewModel.vehiculos.observe(viewLifecycleOwner) { vehiculos ->
            binding.recyclerViewVehiculos.adapter = VehiculoAdapter(vehiculos)
        }

        viewModel.cargarVehiculosDisponibles()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
