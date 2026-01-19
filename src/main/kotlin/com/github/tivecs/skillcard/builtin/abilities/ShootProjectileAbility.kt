package com.github.tivecs.skillcard.builtin.abilities

import com.cryptomorin.xseries.XMaterial
import com.github.tivecs.skillcard.core.abilities.Ability
import com.github.tivecs.skillcard.core.abilities.AbilityAttribute
import com.github.tivecs.skillcard.core.abilities.AbilityExecuteResultState
import com.github.tivecs.skillcard.core.abilities.AbilityRequirement
import com.github.tivecs.skillcard.core.abilities.RequirementSource
import com.github.tivecs.skillcard.core.skills.SkillAbility
import com.github.tivecs.skillcard.core.skills.SkillExecutionContext
import com.github.tivecs.skillcard.core.triggers.TriggerAttributeKey
import org.bukkit.Material
import org.bukkit.entity.*
import org.bukkit.event.Event
import org.bukkit.projectiles.ProjectileSource
import org.bukkit.util.Vector

enum class ShootableProjectileType(val projectileClass: Class<out Projectile>) {
    ARROW(Arrow::class.java),
    SNOWBALL(Snowball::class.java),
    FIREBALL(Fireball::class.java),
    SMALL_FIREBALL(SmallFireball::class.java),
    LARGE_FIREBALL(LargeFireball::class.java),
    DRAGON_FIREBALL(DragonFireball::class.java),
    TRIDENT(Trident::class.java),
    SPECTRAL_ARROW(SpectralArrow::class.java),
    EGG(Egg::class.java),
    ENDERPEARL(EnderPearl::class.java),
    WITHER_SKULL(WitherSkull::class.java),
    LLAMA_SPIT(LlamaSpit::class.java),
    FIREWORK(Firework::class.java),
}

data class ShootProjectileAbilityAttribute(
    val shooter: ProjectileSource,
    val type: ShootableProjectileType) : AbilityAttribute {

    companion object {
        const val PROJECTILE_TYPE = "projectile_type"
        const val SHOOTER = "target"
    }
}

object ShootProjectileAbility : Ability<ShootProjectileAbilityAttribute> {
    override val identifier: String = "shoot_projectile"

    override val material: Material
        get() = XMaterial.ARROW.get() ?: Material.ARROW

    override val description: String
        get() = "&fShoots a projectile from the shooter"

    override fun execute(attribute: ShootProjectileAbilityAttribute?): AbilityExecuteResultState {
        if (attribute == null) return AbilityExecuteResultState.INVALID_ATTRIBUTE

        attribute.shooter.launchProjectile(attribute.type.projectileClass)

        return AbilityExecuteResultState.EXECUTED
    }

    override fun <TEvent : Event> createAttribute(
        context: SkillExecutionContext<TEvent>,
        skillAbility: SkillAbility
    ): ShootProjectileAbilityAttribute? {
        val trigger = context.skillBookContext.getTrigger()
        val triggerResult = context.skillBookContext.getTriggerResult()

        val shooter = trigger.getTarget(triggerResult, TriggerAttributeKey.TARGET_TYPE.key) as? ProjectileSource ?: return null

        val projectileTypeStr = skillAbility.abilityAttributes[ShootProjectileAbilityAttribute.PROJECTILE_TYPE] as? String ?: return null
        val projectileType = ShootableProjectileType.valueOf(projectileTypeStr.uppercase())

        return ShootProjectileAbilityAttribute(shooter, projectileType)
    }

    override fun getRequirements(): List<AbilityRequirement> {
        return listOf(
            AbilityRequirement(
                key = ShootProjectileAbilityAttribute.PROJECTILE_TYPE,
                targetType = ShootableProjectileType::class,
                source = RequirementSource.USER_CONFIGURED,
                defaultValue = ShootableProjectileType.ARROW,
                choices = ShootableProjectileType.entries.map { it.name }
            ),
            AbilityRequirement(
                key = ShootProjectileAbilityAttribute.SHOOTER,
                targetType = ProjectileSource::class,
                source = RequirementSource.TRIGGER
            ),
        )
    }
}