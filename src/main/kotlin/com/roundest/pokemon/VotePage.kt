package com.roundest.pokemon

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize

fun votePage(first: Pokemon, second: Pokemon): String {
    return writePage {
        head {
            scripts()
            title { +"Roundest" }
        }
        body {
            classes = setOf(
                "min-w-screen",
                "flex",
                "min-h-screen",
                "flex-col",
                "justify-between",
                "border-t-2",
                "border-green-600",
                "bg-gray-950",
                "text-white",
                "antialiased"
            )
            navbar()
            vote(first, second)
            footer()
        }
    }
}


fun voteFragment(first: Pokemon, second: Pokemon): String {
    return writePage {
        body {
            div {
                classes = setOf("flex", "justify-center", "gap-16", "items-center", "min-h-[80vh]")
                pokemonVote(first, second.dexId)
                pokemonVote(second, first.dexId)
            }
        }
    }
}

fun voteResultsPage(votes: List<Pokemon>): String {
    return writePage {
        head {
            scripts()
            title { +"Results" }
        }
        body {
            classes = setOf(
                "min-w-screen",
                "flex",
                "min-h-screen",
                "flex-col",
                "justify-between",
                "border-t-2",
                "border-green-600",
                "bg-gray-950",
                "text-white",
                "antialiased"
            )
            navbar()
            main {
                classes = setOf("flex-1")
                div {
                    classes = setOf("container", "mx-auto", "px-4", "py-8", "text-white")
                    div {
                        classes = setOf("grid", "gap-4")
                        votes.forEachIndexed { index, vote ->
                            div {
                                classes = setOf(
                                    "flex",
                                    "items-center",
                                    "gap-6",
                                    "rounded-lg",
                                    "bg-gray-800/40",
                                    "p-6",
                                    "shadow",
                                    "transition-shadow",
                                    "hover:shadow-md"
                                )
                                div {
                                    classes = setOf("w-8", "text-2xl", "font-bold", "text-gray-400")
                                    +"#${index + 1}"
                                }

                                img {
                                    src =
                                        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${vote.dexId}.png"
                                    alt = vote.name
                                    style = "image-rendering: pixelated;"
                                    classes = setOf("h-20", "w-20", "sprite")
                                }

                                div {
                                    classes = setOf("flex-grow")
                                    div {
                                        classes = setOf("text-sm text-gray-400")
                                        +"#${vote.dexId}"
                                        h2 {
                                            classes = setOf("text-xl", "font-semibold", "capitalize")
                                            +vote.name
                                        }
                                    }
                                }
                                div {
                                    classes = setOf("text-right")
                                    div {
                                        classes = setOf("text-2xl", "font-bold", "text-blue-400")
                                        +String.format("%.1f%%", vote.winPercentage)
                                    }
                                    div {
                                        classes = setOf("text-sm", "text-gray-400")
                                        +"${vote.upVotes}W - ${vote.downVotes}L"
                                    }
                                }
                            }
                        }
                    }
                }
            }
            footer()
        }
    }
}

private fun FlowContent.navbar() {
    header {
        classes = setOf("px-8", "py-4")
        attributes["hx-boost"] = "true"
        div {
            classes = setOf("flex", "justify-between", "items-center")
            div {
                classes = setOf("flex", "items-baseline")
                a {
                    classes = setOf("font-bold", "text-3xl")
                    href = "/"
                    +"round"
                    span {
                        classes = setOf("text-green-300")
                        +"est"
                    }
                    span {
                        classes = setOf("text-gray-400", "font-extralight", "pl-2", "text-2xl")
                        +"(Spring Boot + HTMX)"
                    }
                }
            }
            nav {
                classes = setOf("flex", "flex-row", "items-center", "gap-8")
                a {
                    classes = setOf("hover:underline", "text-lg")
                    href = "/results"
                    +"Results"
                }
            }
        }
    }
}

private fun FlowContent.pokemonVote(pokemon: Pokemon, loser: Int) {
    div {
        classes = setOf("flex", "flex-col", "items-center", "gap-4")
        img {
            src = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${pokemon.dexId}.png"
            alt = pokemon.name
            style = "image-rendering: pixelated;"
            classes = setOf("w-64", "h-64", "sprite")
        }
        div {
            classes = setOf("text-center")
            span {
                classes = setOf("text-gray-500", "text-lg")
                +"#${pokemon.dexId}"
            }
            h2 {
                classes = setOf("text-2xl", "font-bold", "capitalize")
                +pokemon.name
            }
            form {
                classes = setOf("mt-4")
                input { type = InputType.hidden; name = "winnerDexId"; value = pokemon.dexId.toString() }
                input { type = InputType.hidden; name = "loserDexId"; value = loser.toString() }
                button {
                    attributes["hx-post"] = "/vote"
                    attributes["hx-target"] = "#vote"
                    classes = setOf(
                        "px-8",
                        "py-3",
                        "bg-blue-500",
                        "text-white",
                        "rounded-lg",
                        "text-lg",
                        "font-semibold",
                        "hover:bg-blue-600",
                        "transition-colors"
                    )
                    +"Vote"
                }
            }
        }
    }
}

private fun FlowContent.vote(first: Pokemon, second: Pokemon) {
    main {
        classes = setOf("flex-1")
        div {
            classes = setOf("container", "mx-auto", "px-4")
            id = "vote"
            div {
                classes = setOf("flex", "justify-center", "gap-16", "items-center", "min-h-[80vh]")
                pokemonVote(first, second.dexId)
                pokemonVote(second, first.dexId)
            }
        }
    }
}

private fun FlowContent.footer() {
    footer {
        classes = setOf("font-light", "text-center", "text-gray-500", "py-3")
        a {
            href = "https://github.com/sundermann/roundest"
            target = ATarget.blank
            +"GitHub"
        }
    }
}

private fun HEAD.scripts() {
    script { src = "https://cdn.tailwindcss.com" }
    script { src = "/webjars/htmx.org/2.0.3/dist/htmx.min.js" }
}

inline fun writePage(crossinline block : HTML.() -> Unit): String {
    return createHTMLDocument().html {
        lang = "en"
        visit(block)
    }.serialize()
}