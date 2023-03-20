package com.example.pokemonster

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.pokemonster.di.AppModule
import com.example.pokemonster.io.local.PokemonDatabase
import com.example.pokemonster.io.local.entities.PokemonEntity
import com.example.pokemonster.io.local.entities.PokemonMoveEntity
import com.example.pokemonster.io.local.entities.PokemonStatEntity
import com.example.pokemonster.view.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(AppModule::class)
@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var db: PokemonDatabase
    lateinit var pokmonList: ArrayList<PokemonEntity>
    lateinit var pokmonStateList: ArrayList<PokemonStatEntity>
    lateinit var pokmonMovesList: ArrayList<PokemonMoveEntity>

    @Before
    fun setUp() {
        hiltRule.inject()
        pokmonList = arrayListOf(
            PokemonEntity(
                1,
                "abc",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                1,
                "abc",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                2,
                "efg",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                3,
                "hij",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                4,
                "klm",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                5,
                "nop",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            ),
            PokemonEntity(
                6,
                "qrs",
                "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                false
            )
        )

        pokmonStateList = arrayListOf(
            PokemonStatEntity("hp-1", 1, "hp", 10),
            PokemonStatEntity("hpw-1", 1, "hpw", 10),
            PokemonStatEntity("hpe-1", 1, "hpe", 10),
            PokemonStatEntity("hpr-1", 1, "hpr", 10),
            PokemonStatEntity("hpt-1", 1, "hpt", 10),
            PokemonStatEntity("hpc-2", 2, "hpc", 10),
            PokemonStatEntity("hpv-2", 2, "hpv", 10),
            PokemonStatEntity("hpb-2", 2, "hpb", 10),
            PokemonStatEntity("hpn-2", 2, "hpn", 10),
            PokemonStatEntity("hpm-2", 2, "hpm", 10),
            PokemonStatEntity("mm-3", 3, "mm", 10),
            PokemonStatEntity("hh-3", 3, "hh", 10),
            PokemonStatEntity("kk-3", 3, "kk", 10),
            PokemonStatEntity("rr-3", 3, "rr", 10),
            PokemonStatEntity("xx-3", 3, "xx", 10)
        )

        pokmonMovesList = arrayListOf(
            PokemonMoveEntity("1-1", 1, 1, "hp", "dfodf"),
            PokemonMoveEntity("2-1", 1, 2, "hpw", "efrty"),
            PokemonMoveEntity("3-1", 1, 3, "hpe", "jdfvsd"),
            PokemonMoveEntity("4-1", 1, 4, "hpr", "dfcd"),
            PokemonMoveEntity("5-1", 1, 5, "hpt", "nhgbv"),
            PokemonMoveEntity("1-2", 2, 1, "hpc", "hhdol"),
            PokemonMoveEntity("2-2", 2, 2, "hpv", "plsjm"),
            PokemonMoveEntity("3-2", 2, 3, "hpb", "sadfc"),
            PokemonMoveEntity("4-2", 2, 4, "hpn", "hwefbjadh"),
            PokemonMoveEntity("5-2", 2, 5, "hpm", "nkasdc"),
            PokemonMoveEntity("1-3", 3, 1, "mm", "oeihwe"),
            PokemonMoveEntity("2-3", 3, 2, "hh", "cmnxv"),
            PokemonMoveEntity("3-3", 3, 3, "kk", "hbcvn"),
            PokemonMoveEntity("4-3", 3, 4, "rr", "psado"),
            PokemonMoveEntity("5-3", 3, 5, "xx", "bfdwertgg")
        )

        runTest {
            db.pokemonDao().insertAllPokemons(
                PokemonEntity(
                    1,
                    "abc",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    1,
                    "abc",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    2,
                    "efg",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    3,
                    "hij",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    4,
                    "klm",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    5,
                    "nop",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                ),
                PokemonEntity(
                    6,
                    "qrs",
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/1.png",
                    false
                )
            )

            db.pokemonDao().insertAllStates(
                PokemonStatEntity("1-1", 1, "hp", 10),
                PokemonStatEntity("2-1", 1, "hpw", 10),
                PokemonStatEntity("3-1", 1, "hpe", 10),
                PokemonStatEntity("4-1", 1, "hpr", 10),
                PokemonStatEntity("5-1", 1, "hpt", 10),
                PokemonStatEntity("1-2", 2, "hpc", 10),
                PokemonStatEntity("2-2", 2, "hpv", 10),
                PokemonStatEntity("3-2", 2, "hpb", 10),
                PokemonStatEntity("4-2", 2, "hpn", 10),
                PokemonStatEntity("5-2", 2, "hpm", 10),
                PokemonStatEntity("1-3", 3, "mm", 10),
                PokemonStatEntity("2-3", 3, "hh", 10),
                PokemonStatEntity("3-3", 3, "kk", 10),
                PokemonStatEntity("4-3", 3, "rr", 10),
                PokemonStatEntity("5-3", 3, "xx", 10)
            )

            db.pokemonDao().insertAllPokemonMoves(
                PokemonMoveEntity("1-1", 1, 1, "hp", "dfodf"),
                PokemonMoveEntity("2-1", 1, 2, "hpw", "efrty"),
                PokemonMoveEntity("3-1", 1, 3, "hpe", "jdfvsd"),
                PokemonMoveEntity("4-1", 1, 4, "hpr", "dfcd"),
                PokemonMoveEntity("5-1", 1, 5, "hpt", "nhgbv"),
                PokemonMoveEntity("1-2", 2, 1, "hpc", "hhdol"),
                PokemonMoveEntity("2-2", 2, 2, "hpv", "plsjm"),
                PokemonMoveEntity("3-2", 2, 3, "hpb", "sadfc"),
                PokemonMoveEntity("4-2", 2, 4, "hpn", "hwefbjadh"),
                PokemonMoveEntity("5-2", 2, 5, "hpm", "nkasdc"),
                PokemonMoveEntity("1-3", 3, 1, "mm", "oeihwe"),
                PokemonMoveEntity("2-3", 3, 2, "hh", "cmnxv"),
                PokemonMoveEntity("3-3", 3, 3, "kk", "hbcvn"),
                PokemonMoveEntity("4-3", 3, 4, "rr", "psado"),
                PokemonMoveEntity("5-3", 3, 5, "xx", "bfdwertgg")
            )
        }
    }

    @Test
    fun isShowing_the_correct_title() {
        composeRule.onNode(
            hasText(composeRule.activity.getString(R.string.home_title))
        ).assertExists()
    }

    @Test
    fun searchTest() = runTest {
        delay(2000)
        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.search_placeholder)
        ).assertExists()

        composeRule.onNodeWithText(
            composeRule.activity.getString(R.string.search_placeholder)
        ).performTextInput(pokmonList[1].name)

        composeRule.onAllNodes(
            hasText(pokmonList[1].name)
        )[1].assertExists()
    }

    @Test
    fun getsAllThePokmons() = runTest {
        delay(2000)
        composeRule.onNode(
            hasText(pokmonList[1].name)
        ).assertExists()
    }

    @Test
    fun NavigateToStatesAndBackToHomeScreen() = runTest {
        delay(4000)
        composeRule.onNode(
            hasText(pokmonList[0].name)
                and
                    hasClickAction()
        ).assertExists()

        composeRule.onNode(
            hasText(pokmonList[0].name)
                and
                    hasClickAction()
        ).performClick()

        composeRule.onAllNodes(
            hasText(pokmonList[0].name)
                and
                    !hasClickAction()
        )[1].assertExists()

        composeRule.onNode(
            hasText(pokmonStateList[0].name)
                and
                    !hasClickAction()
        ).assertExists()

        composeRule.onNode(
            hasText(pokmonMovesList[0].name)
                and
                    hasClickAction()
        ).assertExists()
        composeRule.onNode(
            hasText(pokmonMovesList[0].name)
                and
                    hasClickAction()
        ).performClick()

        composeRule.onAllNodes(
            hasText(pokmonMovesList[0].name)
                and
                    !hasClickAction()
        )[1].assertExists()
    }
}