package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResult
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.util.Vector

data class ShootProjectileAbilityAttribute(
    val shooter: LivingEntity,
    val velocity: Vector,
    val projectileClass: Class<out Projectile>)

object ShootProjectileAbility : Ability<ShootProjectileAbilityAttribute> {
    override val identifier: String = "shoot_projectile"

    override val material: Material
        get() = XMaterial.ARROW.get() ?: Material.ARROW

    override val description: String
        get() = "&fShoots a projectile from the shooter with the specified velocity."

    override fun execute(attribute: ShootProjectileAbilityAttribute): AbilityExecuteResult {
        attribute.shooter.launchProjectile(attribute.projectileClass, attribute.velocity)

        return AbilityExecuteResult.EXECUTED
    }
}