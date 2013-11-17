package controllers

import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Redirect(routes.Products.list())
    //Ok(views.html.index("Your new application ils ready."))
  }

  def hello(name: String) = Action {
    Ok(views.html.hello(name))
  }
}