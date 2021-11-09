package presenter

import view.ConsoleView

interface Presenter {
    fun setupWithView(view: ConsoleView)
    fun start(fileName: String)
    fun search(query: String)
}