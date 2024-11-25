Feature: Movie Controller API
  As a user
  I want to test CRUD operation related to Movie Controller
  So that I stay updated with latest movie details

  Background:
    * def movieId = java.util.UUID.randomUUID()

  Scenario: Create a movie
    Given the Movie API is available
    When a POST request is made to "/apis/v1/movies" with the following JSON request body:
    """
    {
    "id": "#(movieId)",
    "title": "3 Idiots",
    "year": "2010-07-10",
    "genre": "comedy",
    "country": "India",
    "duration": 190,
    "language": "Hindi"
    }
    """
    Then the response status code should be 200
    And the response should contain the following JSON:
      """
      {
    "id": "#(movieId)",
    "title": "3 Idiots",
    "year": "2010-07-10",
    "genre": "comedy",
    "country": "India",
    "duration": 190,
    "language": "Hindi"
    }
      """