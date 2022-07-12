//
//  RepositoriesListViewController.swift
//  GitHubClient
//
//  Created by Ilya Shevtsov on 23.06.2022.
//

import UIKit
import MaterialComponents.MaterialActivityIndicator
import shared

class RepositoriesListViewController: UIViewController {
    
    @IBOutlet private weak var tableView: UITableView!
    @IBOutlet private weak var errorView: ErrorView!
    @IBOutlet private weak var activityIndicator: MDCActivityIndicator!
    
    private let appRepo = DI.shared.appRepo
    
    private var repos: Array<Repo> = []
    private let cellIdentifier = "RepositoryCell"
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.tableView.delegate = self
        self.tableView.dataSource = self
        
        setUI()
        getData()
    }
    
    private func setUI() {
        tableView.register(UINib(nibName: String(describing: RepositoryTableViewCell.self), bundle: nil), forCellReuseIdentifier: cellIdentifier)
        
        activityIndicator.setColor()
        activityIndicator.radius = 28
        
        title = NSLocalizedString("reposList.screenTitle", comment: "")
        navigationItem.backButtonTitle = ""
        setExitButton()
    }
    
    private func getData() {
        loadingStart()
        appRepo.getRepositories { [weak self] repos, error in
            self?.activityIndicator.hide()
            if let repos = repos {
//                self?.hideErrorView(self?.errorView)
                self?.repos = repos
                self?.tableView.reloadData()
                self?.tableView.isHidden = false
                return
            }
            
            if let error = error {
//                self?.showErrorView(self?.errorView, error: error) { self?.getData() }
            }
        }
    }
    
    private func loadingStart() {
//        hideErrorView(errorView)
        tableView.isHidden = true
        activityIndicator.show()
    }
}

extension RepositoriesListViewController: UITableViewDataSource {
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return repos.count
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        let cell = tableView.dequeueReusableCell(withIdentifier: cellIdentifier, for: indexPath) as! RepositoryTableViewCell
        cell.setData(repo: repos[indexPath.row])
        return cell
    }
}

extension RepositoriesListViewController: UITableViewDelegate {
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {
        let nextViewController = RepositoryDetailInfoViewController(repo: repos[indexPath.row], nibName: String(describing: RepositoryDetailInfoViewController.self), bundle: nil)
        navigationController?.pushViewController(nextViewController, animated: true)
    }
}
