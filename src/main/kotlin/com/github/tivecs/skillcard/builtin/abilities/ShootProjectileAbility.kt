package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.util.Vector

enum class ShootableProjectileType(val projectileClass: Class<out Projectile>) {
    ARROW(Arrow::class.java),
    SNOWBALL(Snowball::class.java),
    FIREBALL(Fireball::class.java),
}

data class ShootProjectileAbilityAttribute(
    val shooter: LivingEntity,
    val velocity: Vector,
    val type: ShootableProjectileType) : AbilityAttribute {

    companion object {
        const val VELOCITY_KEY = "velocity"
        const val SHOOTER_KEY = "shooter"
        const val TYPE_KEY = "type"
    }
}

object ShootProjectileAbility : Ability<ShootProjectileAbilityAttribute> {
    override val identifier: String = "shoot_projectile"

    override val material: Material
        get() = XMaterial.ARROW.get() ?: Material.ARROW

    override val description: String
        get() = "&fShoots a projectile from the shooter with the specified velocity."

    override fun execute(attribute: ShootProjectileAbilityAttribute): AbilityExecuteResultState {
        attribute.shooter.launchProjectile(attribute.type.projectileClass, attribute.velocity)

        return AbilityExecuteResultState.EXECUTED
    }
}